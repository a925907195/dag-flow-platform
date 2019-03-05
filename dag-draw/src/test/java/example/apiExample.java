package example;

import org.enoir.graphvizapi.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by frank on 2014/11/20.
 */
public class apiExample {

    private static String tmpPath = "dag-draw/sample";
    public static void main(String[] args)
    {
        apiExample ex = new apiExample();
        ex.draw();
        ex.draw2();
        ex.draw3();
    }
    private void draw()
    {
        Graphviz gv = new Graphviz();
        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        graph.addAttribute(new Attribute("rankdir", "LR"));
        Node n1 = new Node("N1");
        n1.addAttribute(new Attribute("label", "\" Node1 \""));
        Node n2 = new Node("N2");
        Node n3 = new Node("N3");

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addEdge(new Edge("", n1, n2));
        graph.addEdge(new Edge("", n2, n3));
        graph.addEdge(new Edge("",n3,n1));


        String type = "png";

        File out = new File(tmpPath+"/outEX1."+ type);
        this.writeGraphToFile( gv.getGraphByteArray(graph, type, "100"), out );
    }

    public void draw2(){
        int nodeNumber = 10;
        int edageNumber = 20;
        Graphviz gv = new Graphviz();
        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        Graph subgraph = new Graph("subg1", GraphType.DIGRAPH);
        List<Node> nodeList = new ArrayList<Node>();
        for(int i=0;i<nodeNumber;i++){
            Node n = new Node("N"+Integer.toString(i));
            nodeList.add(n);
            graph.addNode(n);
        }
        for(int e=0;e<edageNumber;e++){
            Random ran = new Random();
            int f = ran.nextInt(nodeNumber);
            int t = ran.nextInt(nodeNumber);
            Edge edge = new Edge("",nodeList.get(f),nodeList.get(t));
            graph.addEdge(edge);
        }
         for(int i=0;i<nodeNumber/2;i++){
            Node n = new Node("sN"+Integer.toString(i));
             n.addAttribute(new Attribute("style", "filled"));
            nodeList.add(n);
            subgraph.addNode(n);
        }
        for(int e=0;e<edageNumber;e++){
            Random ran = new Random();
            int f = ran.nextInt(nodeList.size());
            int t = ran.nextInt(nodeList.size());
            Edge edge = new Edge("",nodeList.get(f),nodeList.get(t));
            subgraph.addEdge(edge);
        }
        graph.addSubgraph(subgraph);
        String type = "png";
        File out = new File(tmpPath+"/outEX2."+ type);
        this.writeGraphToFile( gv.getGraphByteArray(graph, type, "100"), out );

    }

    public void draw3(){
        Graphviz gv = new Graphviz();
        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        Graph cluster_0 = new Graph("cluster_0",GraphType.DIGRAPH);
        cluster_0.addAttribute(new Attribute("style","filled"));
        cluster_0.addAttribute(new Attribute("color","lightgrey"));
        cluster_0.addAttribute(new Attribute("label","\"process #1\""));
        Attribute cn0Attr = new Attribute("style","filled");
        Node a0 = new Node("a0");
        Node a1 = new Node("a1");
        Node a2 = new Node("a2");
        Node a3 = new Node("a3");
        cluster_0.addNode(a0);
        cluster_0.addNode(a1);
        cluster_0.addNode(a2);
        cluster_0.addNode(a3);
        cluster_0.addEdge(new Edge("",a0,a1));
        cluster_0.addEdge(new Edge("",a1,a2));
        cluster_0.addEdge(new Edge("",a2,a3));
        Graph cluster_1 = new Graph("cluster_1",GraphType.DIGRAPH);
        cluster_1.addAttribute(new Attribute("color","blue"));
        cluster_1.addAttribute(new Attribute("label","\"process #1\""));
        Node b0 = new Node("b0");
        Node b1 = new Node("b1");
        Node b2 = new Node("b2");
        Node b3 = new Node("b3");
        cluster_1.addNode(b0);
        cluster_1.addNode(b1);
        cluster_1.addNode(b2);
        cluster_1.addNode(b3);
        cluster_1.addEdge(new Edge(b0,b1));
        cluster_1.addEdge(new Edge(b1,b2));
        cluster_1.addEdge(new Edge(b2,b3));
        Node startNode = new Node("Start");
        startNode.addAttribute(new Attribute("shape","Mdiamond"));
        Node endNode = new Node("End");
        endNode.addAttribute(new Attribute("shape","Msquare"));
        graph.addNode(startNode);
        graph.addNode(endNode);
        graph.addSubgraph(cluster_0);
        graph.addSubgraph(cluster_1);
        graph.addEdge(new Edge(startNode, a0));
        graph.addEdge(new Edge(startNode, b0));
        graph.addEdge(new Edge(a1,b3));
        graph.addEdge(new Edge(b2,a3));
        graph.addEdge(new Edge(a3,a0));
        graph.addEdge(new Edge(a3, endNode));
        graph.addEdge(new Edge(b3, endNode));
        String type = "png";
        File out = new File(tmpPath+"/outEX3."+ type);
        this.writeGraphToFile( gv.getGraphByteArray(graph, type, "100"), out );
    }

    public int writeGraphToFile(byte[] img, File to)
    {
        try {
            FileOutputStream fos = new FileOutputStream(to);
            fos.write(img);
            fos.close();
        } catch (java.io.IOException ioe) { return -1; }
        return 1;
    }
}
