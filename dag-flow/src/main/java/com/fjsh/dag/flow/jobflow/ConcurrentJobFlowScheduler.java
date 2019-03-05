package com.fjsh.dag.flow.jobflow;

import com.google.common.collect.Maps;

import com.fjsh.dag.flow.common.ResultStatus;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import rx.Observable;
import rx.subjects.AsyncSubject;

/**
 * 并发JobFlow任务调度器。
 */
public class ConcurrentJobFlowScheduler extends JobFlowScheduler {
    
    private ConcurrentMap<JobFlowRunner, JobState> jobStates = Maps.newConcurrentMap();
    
    private HystrixThreadPoolProperties.Setter hystrixSetter, cpuInsentiveHystrixSetter;
    
    private HystrixThreadPoolKey threadPoolKey, cpuInsentiveThreadPoolKey;


    ConcurrentJobFlowScheduler(JobFlow graph,String group) {
        super(graph,group);
        this.threadPoolKey = HystrixThreadPoolKey.Factory.asKey(graph.getJobFlowKey());
        this.cpuInsentiveThreadPoolKey = HystrixThreadPoolKey.Factory.asKey(graph.getJobFlowKey() + "_CPU_INSENTIVE");
        this.hystrixSetter = HystrixThreadPoolProperties.Setter().withCoreSize(graph.getThreadNumber());
        this.cpuInsentiveHystrixSetter = HystrixThreadPoolProperties.Setter()
                .withCoreSize(graph.getCpuInsentiveThreadNumber())
                .withMaxQueueSize(1000).withQueueSizeRejectionThreshold(1000);
    }

    /**
     * 异步执行任务。
     */
    protected JobState queueJob(JobFlowRunner runner) throws Exception {
        Map<JobFlowRunner, Set<JobFlowRunner>> jobMap = graph.graphMap.get(group).jobs;
        if (runner == null || !jobMap.containsKey(runner)) {
            throw new Exception("Job not registered : " + runner);
        }

        if (!jobStates.containsKey(runner)) {
            JobState state = new JobState(runner);
            JobState preState = jobStates.putIfAbsent(runner, state);
            if (preState == null) {
                try {
                    Set<JobFlowRunner> depends = jobMap.get(runner);
                    Observable<ResultStatus> ob;
                    if (depends != null && depends.size() > 0) {
                        final List<Observable<ResultStatus>> obs = new ArrayList<>(depends.size());
                        final List<JobState> states = new ArrayList<>(depends.size());
                        // 先启动依赖的任务
                        for (JobFlowRunner depend : depends) {
                            JobState dependState = queueJob(depend);
                            obs.add(dependState.getObservable());
                            states.add(dependState);
                        }
                        //observe()和toObservable()虽然都返回了Observable对象，但是observe()返回的是Hot Observable，所以我们用ho作为引用名，该命令会在observe()调用的时候立即执行，当Observable每次被订阅的时候会重放他的行为；而toObservable()返回的是Cold Observable，我们用co作为引用名，toObservable()执行之后，命令不会被立即执行，只有当所有订阅者都订阅它之后才会执行
                        // 依赖执行完成后, 需要将状态都设置成done
                        ob = Observable.mergeDelayError(obs)
                            .ignoreElements() //不发送任何数据,只发送Observable的终止数据，只执行oncomplate或者onerror方法，next不执行
                            .concatWith(state.toObservable())  //先处理依赖的job,最后处理本身的job，
                            .last();  //只发送最后一个
                    } else {
                        ob = state.toObservable();
                    }
                    AsyncSubject<ResultStatus> subject = AsyncSubject.create();
                    ob.subscribe(subject);    //异步执行
                    state.observable = subject;
                } finally {
                    state.started.countDown();
                }
            }
        }
        return jobStates.get(runner);
    }
    
    @Override
    public <T extends JobFlowRunner> Future<ResultStatus> submitJob(T runner) throws Exception {
        JobState state = queueJob(runner);
        return state.getObservable().toBlocking().toFuture();
    }
    
    /**
     * 同步执行任务。
     */
    @Override
    public <T extends JobFlowRunner> void doJob(T runner) throws Exception {
        JobState state = queueJob(runner);
        state.getObservable().toBlocking().toFuture().get();
    }
    
    /**
     * 批量同步执行任务。
     */
    @Override
    public void doJob(List<? extends JobFlowRunner> runners) throws Exception {
        List<JobState> states = new ArrayList<>();
        for (JobFlowRunner runner : runners) {
            states.add(queueJob(runner));
        }
        for (JobState state : states) {
            state.getObservable().toBlocking().toFuture().get();
        }
    }
    
    private class JobState extends HystrixCommand<ResultStatus> {
        private  Logger logger = LoggerFactory.getLogger(JobState.class);
        /**
         * 任务是否已经开始执行
         */
        private final CountDownLatch started = new CountDownLatch(1);
        
        /**
         * job对应的已启动的Observable
         */
        private Observable<ResultStatus> observable;
        
        private JobFlowRunner runner;
        
        public JobState(JobFlowRunner runner) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(graph.getJobFlowKey()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(runner.getJobKey()))
                        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(runner.timeout)
                            .withCircuitBreakerEnabled(false))
                        .andThreadPoolKey(runner.isCpuIntensive() ? 
                                ConcurrentJobFlowScheduler.this.cpuInsentiveThreadPoolKey : ConcurrentJobFlowScheduler.this.threadPoolKey)
                        .andThreadPoolPropertiesDefaults(runner.isCpuIntensive() ? 
                                ConcurrentJobFlowScheduler.this.cpuInsentiveHystrixSetter : ConcurrentJobFlowScheduler.this.hystrixSetter));
            this.runner = runner;
        }
        
        public Observable<ResultStatus> getObservable() throws InterruptedException {
            // 等待任务启动完成
            started.await();
            return observable;
        }

        @Override
        protected ResultStatus run() throws Exception {
            return runner.run(ConcurrentJobFlowScheduler.this);
        }

        @Override
        protected ResultStatus getFallback() {
            logger.info("Hystrix fallback: jobkey="+runner.getJobKey()+", timeout="+isResponseTimedOut()+":"+getExecutionTimeInMilliseconds());
            runner.fallback(ConcurrentJobFlowScheduler.this);
            return ResultStatus.FALLBACK;
        }
        
    }

}
