package org.enoir.graphvizapi.test;

import org.enoir.graphvizapi.Attribute;
import org.enoir.graphvizapi.Graph;
import org.enoir.graphvizapi.GraphType;
import org.enoir.graphvizapi.Graphviz;
import org.enoir.graphvizapi.Node;
import org.enoir.graphvizapi.exception.AttributeNotFondException;
import org.enoir.graphvizapi.exception.GraphException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by frank on 2014/11/27.
 */
public class TestException {
    @Test
    public void TestAttributeNotFond(){
        try {
            Node node = new Node("testId");
            node.addAttribute(new Attribute("color", "#000"));
            node.removeAttribute("label");
        }catch (AttributeNotFondException e) {
            Assert.assertTrue(true);
        }catch (Exception ex){
            Assert.assertTrue(false);
        }
    }

    @Test
    public void TestDotCommandNotFound() {
        Graphviz gz = new Graphviz("/usr/bin/dot1","./");
        Graph graph = new Graph("g1", GraphType.DIGRAPH);
        String type = "png";
        try {
            byte[] bytearray = gz.getGraphByteArray(graph, type, "100");
            Assert.assertTrue(false);
        }catch (GraphException ge){
            Assert.assertTrue(true);
        }
    }


}

