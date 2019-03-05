package com.fjsh.dag.flow.jobflow;


import com.google.common.collect.Sets;

import com.fjsh.dag.flow.common.ResultStatus;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

/**
 * 串行JobFlow任务调度器，一般Job均为无IO操作的CPU密集型任务。
 * 线程不安全，不支持并发访问。
 */
@Slf4j
public class SerialJobFlowScheduler extends JobFlowScheduler {
    
    private Set<JobFlowRunner> finishJobs;
    
    SerialJobFlowScheduler(JobFlow graph) {
        super(graph);
        finishJobs = Sets.newHashSet();
    }

    @Override
    public <T extends JobFlowRunner> Future<ResultStatus> submitJob(T runner) throws Exception {
        throw new Exception("unsupported async job execute.");
    }

    @Override
    public <T extends JobFlowRunner> void doJob(T runner) throws Exception {
        if (finishJobs.contains(runner)) return ;
        try {
            Set<JobFlowRunner> depends = graph.graphMap.get(group).jobs.get(runner);
            for (JobFlowRunner depend : depends) {
                doJob(depend);
            }
            try {
                runner.run(this);
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                runner.fallback(this);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            finishJobs.add(runner);
        }
    }

    @Override
    public void doJob(List<? extends JobFlowRunner> runners) throws Exception {
        for (JobFlowRunner runner : runners) {
            doJob(runner);
        }
    }

}
