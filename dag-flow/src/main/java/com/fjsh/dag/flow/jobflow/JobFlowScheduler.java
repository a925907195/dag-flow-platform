package com.fjsh.dag.flow.jobflow;


import com.fjsh.dag.flow.common.ResultStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * JobFlow任务调度器基类，管理单次Flow请求的所有任务执行。
 */
public abstract class JobFlowScheduler {
    
    protected JobFlow graph;
    protected String group;

    protected Map<String, Object> environment = new ConcurrentHashMap<String, Object>();
    
    JobFlowScheduler(JobFlow graph, String groupName) {
        this.graph = graph;
        this.group=groupName;
    }

    JobFlowScheduler(JobFlow graph) {
        this(graph,JobFlow.DEFAULT_NAME);
    }

    public boolean  isFastMode(){
        return graph.isFastMode();
    }
    
    /**
     * 往JobFlow执行器里增加环境变量。
     */
    @SuppressWarnings("unchecked")
    public <T> T getEnv(String key) {
        return (T) environment.get(key);
    }
    
    /**
     * 从JobFlow执行器去处环境变量。
     */
    public void addEnv(String key, Object value) {
        this.environment.put(key, value);
    }
    
    /**
     * 异步执行任务。
     */
    abstract public <T extends JobFlowRunner> Future<ResultStatus> submitJob(T runner) throws Exception;
    
    /**
     * 同步执行任务。
     */
    abstract public <T extends JobFlowRunner> void doJob(T runner) throws Exception;
    
    /**
     * 批量同步执行任务。
     */
    abstract public void doJob(List<? extends JobFlowRunner> runners) throws Exception;
        
}
