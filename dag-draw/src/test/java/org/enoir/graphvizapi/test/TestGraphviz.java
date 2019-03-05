package org.enoir.graphvizapi.test;

import junit.framework.Assert;

import org.enoir.graphvizapi.Attribute;
import org.enoir.graphvizapi.Edge;
import org.enoir.graphvizapi.Graph;
import org.enoir.graphvizapi.GraphType;
import org.enoir.graphvizapi.Graphviz;
import org.enoir.graphvizapi.Node;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by frank on 2014/11/21.
 */
public class TestGraphviz {

    @Test
    public void testGenImage() {
        Graphviz gz = new Graphviz("/usr/bin/dot","./");
        Graph graph = genSimpleGraph();
        String type = "png";
        byte[] bytearray = gz.getGraphByteArray(graph, type, "100");
        Assert.assertTrue(bytearray.length>0);
    }

    @Test
    public void testGenImageWithDefault() {
        Graphviz gz = new Graphviz();
        gz.setTmpPath("./");
        Graph graph = genSimpleGraph();
        String type = "png";
        byte[] bytearray = gz.getGraphByteArray(graph, type, "100");
        Assert.assertTrue(bytearray.length>0);
    }
    @Test
    public void testGenImageFail() {
        Graphviz gz = new Graphviz("/dot","./");
        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        byte[] bytearray = null;
        try {
            String type = "png";
            bytearray = gz.getGraphByteArray(graph, type, "100");
        }catch (Exception e){

        }
        Assert.assertTrue(bytearray==null);
    }

    private String getByteArrayMd5(byte[] bytes){
        String ret = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            ret = getChecksumString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return ret;
    }

    private Graph genSimpleGraph() {

        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        graph.addAttribute(new Attribute("rankdir", "LR"));
        Node n1 = new Node("N");
        n1.addAttribute(new Attribute("label", "\" Node1 \""));
        Node n2 = new Node("N2");
        Node n3 = new Node("N3");

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addEdge(new Edge("", n1, n2));
        graph.addEdge(new Edge("", n2, n3));
        graph.addEdge(new Edge("",n3,n1));
        graph.addEdge(new Edge("",n3,n1));
        return graph;
    }

    private String getChecksumString(byte[] mdbyte){
        String result = "";
        for (int i=0; i < mdbyte.length; i++) {
            result += Integer.toString( ( mdbyte[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

}
