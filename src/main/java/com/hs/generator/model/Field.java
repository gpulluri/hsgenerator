package com.hs.generator.model;

import java.util.Map;

/**
 * Created by gpulluri on 5/10/17.
 */
public class Field {
    private String name;
    private Integer length;
    private FieldType type;
    private Map<String, String> values;

    public String getName() {
        return name;
    }

    public Integer getLength() {
        return length;
    }

    public FieldType getType() {
        return type;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
