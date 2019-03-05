package org.enoir.graphvizapi;

import org.enoir.graphvizapi.exception.AttributeNotFondException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Graphviz graph base object. It can add one or more attribute to
 * description.
 * Created by frank on 2014/11/19.
 */
public abstract class BaseGraphObject {
    private String id;
    private List<Attribute> attrList;

    /**
     * Constructor.
     * @param id This graph object's id.
     */
    public BaseGraphObject(String id) {
        this.id = id;
        attrList = new ArrayList<Attribute>();
    }

    /**
     * Add an attribute to attribute list.
     * @param attr attribute
     */
    public void addAttribute(Attribute attr){
        this.attrList.add(attr);
    }

    /**
     * Remove attribute by attribute name. If this graph object has two or more attribute
     * with same name, it will remove them.
     * @param attributeName
     */
    public void removeAttribute(String attributeName){
        List<Attribute> removeList = new ArrayList<Attribute>();
        for(Attribute attr : this.attrList){
            if(attr.getAttrName().equals(attributeName)){
                removeList.add(attr);
            }
        }
        if(removeList.size()==0){
            throw new AttributeNotFondException("ID: "+id+";attribute:"+attributeName);
        }
        for(Attribute attr: removeList){
            this.attrList.remove(attr);
        }
    }

    /**
     * Graph object id getter.
     * @return id
     */
    public String getId(){
        return this.id;
    }

    /**
     * Graph object id setter.
     * @param id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Attribute to dot string.
     * @return dot format string.
     */
    protected String genAttributeDotString(){
        StringBuilder attrDotString = new StringBuilder();
        for(Attribute attr : this.attrList){
            attrDotString.append(attr.getAttrName()+"="+attr.getAttrValue()+";\n");
        }
        return attrDotString.toString();
    }

    /**
     * Convert this graph object to graphviz dot format.
     * @return
     */
    abstract public String genDotString();

}
