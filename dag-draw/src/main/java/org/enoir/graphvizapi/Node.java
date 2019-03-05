package org.enoir.graphvizapi;

/**
 * The Node class like Graphviz's node.
 * Created by frank on 2014/11/17.
 */
public class Node extends BaseGraphObject {

    /**
     * Constructor.
     * @param id
     */
    public Node(String id) {
        super(id);
    }

    @Override
    public String genDotString() {
        StringBuilder dotString = new StringBuilder();
        dotString.append("["+this.genAttributeDotString()+"]");
        return dotString.toString();
    }
}
