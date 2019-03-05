package com.fjsh.dag.flow.jobflow;


import com.fjsh.dag.flow.common.Consts;
import com.fjsh.dag.flow.common.ResultStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * JobFlow任务基类。
 */
public abstract class JobFlowRunner {
    
    @Setter
    @Getter
    protected String jobKey;

    @Setter
    protected JobFlow graph;

    
    @Setter
    @Getter
    protected int timeout = Consts.JOB_TIMEOUT_DEFAULT_THRESHOLD;

    @Getter
    protected boolean isCpuIntensive = false;
    
    public JobFlowRunner() {}

    public JobFlowRunner(JobFlow graph, String jobKey) {
        init(graph, jobKey);
    }

    public void init(JobFlow graph, String jobKey) {
        this.graph = graph;
        this.jobKey = jobKey;
    }

    @Override
    public String toString() {
        return jobKey;
    }
    
    @Override
    public int hashCode() {
        return jobKey.hashCode();
    }

    
    @Override
    public boolean equals(Object other) {
        JobFlowRunner runner = (JobFlowRunner) other;
        return jobKey.equals(runner.jobKey);
    }
    
    abstract protected ResultStatus run(JobFlowScheduler scheduler);
    
    abstract protected void fallback(JobFlowScheduler scheduler); 

}
