package org.enoir.graphvizapi;

import org.enoir.graphvizapi.exception.GraphException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Graphviz Graph class. It also can use to build subgraph.
 * Created by frank on 2014/11/20.
 */
public class Graph extends BaseGraphObject {

    private GraphType graphType;
    private List<Node> nodeList;
    private List<Edge> edgeList;
    private List<Graph> subgraphList;

    /**
     * Constructor.
     * @param id This Graph's id.
     * @param graphType Graph's type.
     */
    public Graph(String id, GraphType graphType){
        super(id);
        this.graphType = graphType;
        this.nodeList = new ArrayList<Node>();
        this.edgeList = new ArrayList<Edge>();
        this.subgraphList = new ArrayList<Graph>();
    }

    /**
     * Graph Type getter.
     * @return graph type.
     */
    public GraphType getGraphType(){
        return this.graphType;
    }

    /**
     * Add a node to this graph.
     * @param node
     */
    public void addNode(Node node){
        this.nodeList.add(node);
    }

    /**
     * Add an edge to this graph.
     * @param edge
     */
    public void addEdge(Edge edge){
        this.edgeList.add(edge);
    }

    /**
     * Add subgraph to this graph.
     * @param graph
     */
    public void addSubgraph(Graph graph){
        this.subgraphList.add(graph);
    }

    @Override
    public String genDotString() {
        StringBuilder dotString = new StringBuilder();
        dotString.append("{\n");
        dotString.append(this.genAttributeDotString());
        dotString.append(genSubgraphString());
        dotString.append(genNodesString());
        dotString.append(genEdgeDotString());
        dotString.append("}\n");
        return dotString.toString();
    }

    private String genSubgraphString(){
        StringBuilder subgraphString = new StringBuilder();
        for(Graph graph : subgraphList){
            subgraphString.append("subgraph ");
            subgraphString.append(graph.getId());
            subgraphString.append(graph.genDotString());
            subgraphString.append("\n");
        }
        return subgraphString.toString();
    }

    private String genNodesString(){
        StringBuilder nodeString = new StringBuilder();
        for(Node node: nodeList){
            nodeString.append(node.getId());
            nodeString.append(node.genDotString());
            nodeString.append("\n");
        }
        return nodeString.toString();
    }

    private String genEdgeDotString(){
        StringBuilder edgeString = new StringBuilder();
        for(Edge edge:edgeList){
            edgeString.append(edge.getFromNode().getId()+getLinkStr()+ edge.getToNode().getId());
            edgeString.append(edge.genDotString());
            edgeString.append("\n");
        }
        return edgeString.toString();
    }

    private String getLinkStr(){
        if(this.graphType == GraphType.DIGRAPH){
            return "->";
        }else if(this.graphType == GraphType.GRPAH){
            return "--";
        }else {
            throw new GraphException("Not Support Graph Type.");
        }
    }
}

