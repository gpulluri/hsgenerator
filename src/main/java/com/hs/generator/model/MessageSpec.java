package com.hs.generator.model;

import java.util.*;

/**
 * Created by gpulluri on 5/10/17.
 */
public class MessageSpec {

    private List<Field> fields;
    private MessageHeaderSpec header;
    private MessageBodySpec body;
    private Map<String, Field> fieldMap;
    private Map<String, Integer> fieldOffsetMap;
    private Integer bodyLength;

    public MessageSpec(MessageHeaderSpec header, MessageBodySpec body) {
        this.fields = new ArrayList<>();
        this.header = header;
        this.body = body;
        this.fields.addAll(header.getFields());
        this.fields.addAll(body.getFields());
        this.fieldMap = new LinkedHashMap<>();
        this.fieldOffsetMap = new LinkedHashMap<>();
        int curOffset = 0;
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            fieldOffsetMap.put(field.getName(), curOffset);
            curOffset+=field.getLength();
        }
        bodyLength = curOffset;
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(this.fields);
    }

    public MessageHeaderSpec getHeader() {
        return header;
    }

    public MessageBodySpec getBody() {
        return body;
    }

    public Map<String, Field> getFieldMap() {
        return Collections.unmodifiableMap(fieldMap);
    }

    public int getOffset(String fieldName) {
        return fieldOffsetMap.get(fieldName);
    }

    public int getBodyOffset(String fieldName) {
        return fieldOffsetMap.get(fieldName) - this.header.getLength();
    }

    public Integer getBodyLength() {
        return bodyLength;
    }

    public String getMessageIdentifierValue() {
        return body.getMessageIdentifierValue();
    }

    public FlowDirection getFlowDirection() {
        return body.getFlowDirection();
    }
}
