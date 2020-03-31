package com.hs.generator.model;

import java.util.List;

/**
 * Created by gpulluri on 5/10/17.
 */
public class MessageBodySpec {

    private String messageType;
    private String messageIdentifierValue;
    private FlowDirection flowDirection;
    private List<Field> fields;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageIdentifierValue() {
        return messageIdentifierValue;
    }

    public void setMessageIdentifierValue(String messageIdentifierValue) {
        this.messageIdentifierValue = messageIdentifierValue;
    }

    public FlowDirection getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(FlowDirection flowDirection) {
        this.flowDirection = flowDirection;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
