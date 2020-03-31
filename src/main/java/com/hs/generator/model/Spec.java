package com.hs.generator.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gpulluri on 5/10/17.
 */
public class Spec {
    private String version;
    private String venue;
    private String messageIdentifierField;
    private StaticValue messageStart;
    private StaticValue messageEnd;
    private StaticValue fieldPadding;

    private MessageHeaderSpec messageHeader;
    private List<MessageBodySpec> messages;
    private Map<String, MessageSpec> messageDictionary;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public MessageHeaderSpec getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeaderSpec messageHeader) {
        this.messageHeader = messageHeader;
    }

    public void setMessages(List<MessageBodySpec> messages) {
        this.messages = messages;
    }

    public String getMessageIdentifierField() {
        return messageIdentifierField;
    }

    public void setMessageIdentifierField(String messageIdentifierField) {
        this.messageIdentifierField = messageIdentifierField;
    }

    public StaticValue getMessageStart() {
        return messageStart;
    }

    public void setMessageStart(StaticValue messageStart) {
        this.messageStart = messageStart;
    }

    public StaticValue getMessageEnd() {
        return messageEnd;
    }

    public void setMessageEnd(StaticValue messageEnd) {
        this.messageEnd = messageEnd;
    }

    public StaticValue getFieldPadding() {
        return fieldPadding;
    }

    public void setFieldPadding(StaticValue fieldPadding) {
        this.fieldPadding = fieldPadding;
    }

    public Map<String, MessageSpec> getMessageDictionary() {

        if(this.messageDictionary != null) return this.messageDictionary;
        Map<String, MessageSpec> messageMap = new LinkedHashMap<>();

        this.messages.forEach(mb -> {
            MessageSpec message = new MessageSpec(messageHeader,mb);
            messageMap.put(mb.getMessageType(), message);
        });

        this.messageDictionary = messageMap;

        return messageMap;
    }

    public Field getMessageTypeField() {
        return this.getMessageHeader().getFieldMap().get(messageIdentifierField);
    }
}
