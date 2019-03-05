package org.enoir.graphvizapi;

/**
 * An Attribute Class. It will store graphviz attribute.
 * Created by frank on 2014/11/20.
 */
public class Attribute {
    private String attrName;
    private String attrValue;

    /**
     * @param name Attribute name. Like 'label' 'color'...etc.
     * @param value Attrubute value. Like 'blue'...
     */
    public Attribute(String name, String value){
        this.attrName = name;
        this.attrValue = value;
    }

    /**
     * Attribute name getter.
     * @return attribute name.
     */
    public String getAttrName(){
        return this.attrName;
    }

    /**
     * Attribute value getter.
     * @return attribute value.
     */
    public String getAttrValue(){
        return this.attrValue;
    }

}
