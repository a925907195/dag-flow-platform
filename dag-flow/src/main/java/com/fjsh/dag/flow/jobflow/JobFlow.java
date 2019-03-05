package com.fjsh.dag.flow.jobflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * JobFlow依赖图。
 */
@Getter
public class JobFlow {

    Map<String, SubGraph> graphMap = new HashMap<>();

    private String jobFlowKey;

    @Setter
    private boolean fastMode = false;

    @Setter
    private boolean cacheEnable = false;

    private int threadNumber, cpuInsentiveThreadNumber;

    public static final String DEFAULT_NAME = "default";


    public Map<JobFlowRunner, Set<JobFlowRunner>> getDefaultGraph() {
        return graphMap.get(DEFAULT_NAME).jobs;
    }

    @Data
    public static class SubGraph {
        public SubGraph(String n) {
            this.name = n;
        }

        String name;
        Map<JobFlowRunner, Set<JobFlowRunner>> jobs = new HashMap<>();
    }

    public JobFlow(String jobFlowKey, int threadNumber, int cpuInsentiveThreadNumber) {
        this.jobFlowKey = jobFlowKey;
        this.threadNumber = threadNumber;
        this.cpuInsentiveThreadNumber = cpuInsentiveThreadNumber;
    }

    public JobFlowRunner register(JobFlowRunner job) {
        return register(DEFAULT_NAME, job);
    }

    public synchronized JobFlowRunner register(String name, JobFlowRunner job) {
        if (!graphMap.containsKey(name)) {
            graphMap.put(name, new SubGraph(name));
        }

        Map<JobFlowRunner, Set<JobFlowRunner>> jobs = graphMap.get(name).jobs;
        if (!jobs.containsKey(job)) {
            jobs.put(job, new HashSet<JobFlowRunner>());
        }
        return job;
    }

    public void addDepends(JobFlowRunner job, JobFlowRunner... depends) {
        addDepends(DEFAULT_NAME, job, depends);
    }

    public synchronized void addDepends(String group, JobFlowRunner job, JobFlowRunner... depends) {
        Map<JobFlowRunner, Set<JobFlowRunner>> jobs = graphMap.get(group).jobs;
        Set<JobFlowRunner> jobDepends = jobs.get(job);
        for (JobFlowRunner depend : depends) {
            jobDepends.add(depend);
        }
    }


    private void dfs(JobFlowRunner startJob, SubGraph graph, List<JobFlowRunner> path, Map<JobFlowRunner, Boolean> status) throws Exception {
        status.put(startJob, true);
        path.add(startJob);
        for (JobFlowRunner depend : graph.jobs.get(startJob)) {
            if (status.containsKey(depend)) {
                if (status.get(depend)) {
                    StringBuffer sb = new StringBuffer("Circled graph : ");
                    int i = 0;
                    while (path.get(i) != depend) i++;
                    for (; i < path.size(); i++) {
                        sb.append(path.get(i)).append(" <- ");
                    }
                    sb.append(depend);
                    throw new Exception(sb.toString());
                }
            } else {
                dfs(depend, graph, path, status);
            }
        }
        status.put(startJob, false);
        path.remove(path.size() - 1);
    }

    public synchronized void check() throws Exception {
        //检查未注册的被依赖任务


        for (Entry<String, SubGraph> graph : graphMap.entrySet()) {
            Map<JobFlowRunner, Set<JobFlowRunner>> jobs = graph.getValue().jobs;

            for (Entry<JobFlowRunner, Set<JobFlowRunner>> entry : jobs.entrySet()) {
                for (JobFlowRunner depend : entry.getValue()) {
                    if (!jobs.containsKey(depend)) {
                        throw new Exception("Not registered depends job : " + entry.getKey() + " -> " + depend + ", graph=" + graph.getKey());
                    }
                }
            }
            //检查环形依赖
            Map<JobFlowRunner, Boolean> status = new HashMap<>();
            for (JobFlowRunner job : jobs.keySet()) {
                if (!status.containsKey(job)) {
                    dfs(job, graph.getValue(), new ArrayList<>(), status);
                }
            }
        }

    }


    public JobFlowScheduler createScheduler(String group) {
        return new ConcurrentJobFlowScheduler(this, group);
    }

    public JobFlowScheduler createScheduler() {
        return new ConcurrentJobFlowScheduler(this, JobFlow.DEFAULT_NAME);
    }

}
