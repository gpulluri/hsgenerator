package com.hs.generator.model;

import java.util.*;

/**
 * Created by gpulluri on 5/10/17.
 */
public class MessageHeaderSpec {

    private List<Field> fields;
    private Map<String, Field> fieldMap;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
        if(this.fields == null) return;
        this.fieldMap  = new HashMap<>();
        this.fields.forEach(field -> fieldMap.put(field.getName(), field));
    }

    public Map<String, Field> getFieldMap() {
        if(fieldMap == null){
            fieldMap = new LinkedHashMap<>();
            this.fields.forEach(field -> fieldMap.put(field.getName(), field));
        }
        return Collections.unmodifiableMap(fieldMap);
    }

    public int getLength() {
        int length = 0;
        for (Field field : fields) {
            length+=field.getLength();
        }

        return length;
    }
}
