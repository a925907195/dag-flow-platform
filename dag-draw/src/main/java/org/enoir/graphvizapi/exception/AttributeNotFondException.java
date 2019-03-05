package org.enoir.graphvizapi.exception;

/**
 * The Attribute not found Exception.
 * Created by frank on 2014/11/27.
 */

public class AttributeNotFondException extends GraphvizApiException {
    private final String IDTAG = "GRAPH ID:";
    private final String ATTRTAG = "Attrbute :";
    public AttributeNotFondException(String message) {
        super(message);
    }
}
