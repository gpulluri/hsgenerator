package com.hs.generator.model;

/**
 * Created by gpulluri on 5/16/17.
 */
public class StaticValue {
    private FieldType type;
    private Integer length;
    private String value;

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
