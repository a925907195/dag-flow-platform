package com.fjsh.dag.flow.util;

import com.google.common.collect.Maps;

import com.fjsh.dag.flow.jobflow.JobFlow;
import com.fjsh.dag.flow.jobflow.JobFlowRunner;

import org.enoir.graphvizapi.Attribute;
import org.enoir.graphvizapi.Edge;
import org.enoir.graphvizapi.Graph;
import org.enoir.graphvizapi.GraphType;
import org.enoir.graphvizapi.Graphviz;
import org.enoir.graphvizapi.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: <fujiansheng@.com>
 * @Description： create dag picture in temPath
 * @Date: Created in :2019-02-15 20:46
 * @Modified by:
 */
public class DagGraphUtil {
    public static void createDagPic(JobFlow flow,String tmpPath){
        //create dag picture
        Graphviz gv = new Graphviz();
        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        graph.addAttribute(new Attribute("rankdir", "LR"));
        HashMap<String, Node> nodeHashMap = Maps.newHashMap();
        HashMap<String, Edge> edgeHashMap = Maps.newHashMap();
        DagGraphUtil.createDag(flow, graph,nodeHashMap, edgeHashMap);
        String type = "png";

        File out = new File(tmpPath + "/"+System.currentTimeMillis()+"." + type);
        writeGraphToFile(gv.getGraphByteArray(graph, type, "100"), out);
    }

    private static void createDag(JobFlow flow, Graph daggraph, HashMap<String, Node> nodeHashMap, HashMap<String, Edge> edgeHashMap) {
        for (Map.Entry<String, JobFlow.SubGraph> graph : flow.getGraphMap().entrySet()) {
            Map<JobFlowRunner, Set<JobFlowRunner>> jobs = graph.getValue().getJobs();
            Map<JobFlowRunner, Boolean> status = new HashMap<>();
            for (JobFlowRunner job : jobs.keySet()) {
                Node node = new Node(job.getJobKey());
                nodeHashMap.put(node.getId(), node);
                daggraph.addNode(node);
                dfs(job, graph.getValue(), daggraph, nodeHashMap, edgeHashMap);
            }
        }
    }

    private static void dfs(JobFlowRunner startJob, JobFlow.SubGraph graph, Graph daggraph, HashMap<String, Node> nodeHashMap, HashMap<String, Edge> edgeHashMap) {
        for (JobFlowRunner depend : graph.getJobs().get(startJob)) {
            Node node = new Node(depend.getJobKey());
            nodeHashMap.put(node.getId(), node);
            daggraph.addNode(node);
            Edge edge = new Edge(startJob + "->" + node.getId(), nodeHashMap.get(node.getId()),nodeHashMap.get(startJob.getJobKey()));
            if (!edgeHashMap.containsKey(edge.getId())) {
                edgeHashMap.put(edge.getId(), edge);
                daggraph.addEdge(edge);
            }
            dfs(depend, graph, daggraph, nodeHashMap, edgeHashMap);
        }
    }
    private static int writeGraphToFile(byte[] img, File to) {
        try {
            FileOutputStream fos = new FileOutputStream(to);
            fos.write(img);
            fos.close();
        } catch (java.io.IOException ioe) {
            return -1;
        }
        return 1;
    }
}
