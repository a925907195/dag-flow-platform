package com.fjsh.dag.flow.simple;


import com.fjsh.dag.flow.common.ResultStatus;
import com.fjsh.dag.flow.jobflow.JobFlow;
import com.fjsh.dag.flow.jobflow.JobFlowRunner;
import com.fjsh.dag.flow.jobflow.JobFlowScheduler;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * 一次调用：
 * Benchmark                       Mode  Cnt  Score   Error  Units
 * JobFlowBenchMarkTest.benchTest  avgt   25  0.449 ± 0.073  ms/op
 * 一百次调用：
 * Benchmark                       Mode  Cnt   Score   Error  Units
 * JobFlowBenchMarkTest.benchTest  avgt   25  46.389 ± 8.755  ms/op
 */
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)   // 预热，共5次， 每次500ms
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS, batchSize = 100)  // 测试，共5次， 每次500ms, 以100次调用时间作为测试指标
@BenchmarkMode(Mode.AverageTime)       // 测量值：平均运行时间
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 测量单位：毫秒
public class JobFlowBenchMarkTest {
    @Benchmark
    public static void benchTest() throws Exception {

        JobFlow flow = new JobFlow("demoGraph", 10, 1);
        JobFlowRunner job1 = new DemoJob(flow, "1");
        JobFlowRunner job2 = new DemoJob(flow, "2");
        JobFlowRunner job31 = new DemoJob(flow, "3.1");
        flow.register(job1);
        flow.register(job2);
        flow.register(job31);


        flow.addDepends(job31,job1);

        JobFlowRunner job32 = new DemoJob(flow, "3.2");
        flow.register(job32);
        flow.addDepends(job32, job2);

        JobFlowRunner job4 = new DemoJob(flow, "4");
        flow.register(job4);
        flow.addDepends(job4, job31,job32);

        JobFlowRunner job5 = new DemoJob(flow, "5");
        flow.register(job5);
        flow.addDepends(job5,job31, job32);
//        flow.addDepends(job1,job5);//校验循环依赖
//        flow.check();//检查是否有未注册的依赖以及是否有环

        JobFlowScheduler scheduler = flow.createScheduler();
        scheduler.doJob(job4);
        scheduler.doJob(job5);
    }
    
    private static class DemoJob extends JobFlowRunner {
        
        private String name;
        
        public DemoJob(JobFlow graph, String name) {
            super(graph, name);
            this.name = name;
            this.timeout = 5000;
        }

        @Override
        public ResultStatus run(JobFlowScheduler scheduler) {
            System.out.println("start job " + name);
               int a=1+2;
//            throw new Exception();
            System.out.println("finish job " + name);
            return ResultStatus.SUCCESS;

        }

        @Override
        public void fallback(JobFlowScheduler scheduler) {
            System.out.println("fallback job " + name);
        }
        
    }
    
}
