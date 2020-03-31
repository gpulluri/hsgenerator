package com.hs.generator;

import com.hs.generator.model.Field;
import com.hs.generator.model.FlowDirection;
import com.hs.generator.model.MessageSpec;
import com.hs.generator.model.Spec;
import com.hs.generator.util.GeneratorUtils;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by gpulluri on 5/18/17.
 */
public class ModelGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ModelGenerator.class);

    public static List<TypeSpec> generateMessageModel(Spec adaptorSpec, TypeName superClass) {
        Map<String, MessageSpec> messageDictionary =  adaptorSpec.getMessageDictionary();
        List<TypeSpec> generatedFiles = new ArrayList<>();
        messageDictionary.forEach((k, v) -> {
            generatedFiles.add(generateModel(k, v, superClass));
        });

        return generatedFiles;
    }

    public static TypeSpec generateMessageTypeEnum(Spec adaptorSpec) {
        Map<String, MessageSpec> messageDictionary =  adaptorSpec.getMessageDictionary();

        TypeSpec.Builder messageTypeEnumBuilder = TypeSpec.enumBuilder("MessageType")
                .addModifiers(Modifier.PUBLIC);
        Field messageTypeField = adaptorSpec.getMessageTypeField();
        Type fieldType = GeneratorUtils.getJavaType(messageTypeField.getType(), messageTypeField.getLength());
        messageTypeEnumBuilder.addField(fieldType, "value", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(fieldType, "value")
                        .addStatement("this.$N = $N", "value", "value")
                        .build())
                .addMethod(MethodSpec.methodBuilder("getValue")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return this.$N", "value")
                        .returns(fieldType)
                        .build());

        final String argFormat = messageTypeField.getLength() == 1 ? "'$L'" : "$S";
        messageDictionary.forEach((k, v) -> {
            messageTypeEnumBuilder.addEnumConstant(k, TypeSpec.anonymousClassBuilder(argFormat, v.getMessageIdentifierValue()).build());
        });

        return messageTypeEnumBuilder.build();
    }

    public static TypeSpec generateModel(String messageType, MessageSpec messageSpec, TypeName superClass) {

        TypeSpec.Builder modelBuilder = TypeSpec.classBuilder(GeneratorUtils.getClassName(messageType))
                .superclass(superClass)
                .addModifiers(Modifier.PUBLIC);
        final FlowDirection flowDirection = messageSpec.getFlowDirection();

        messageSpec.getBody().getFields().forEach(field -> {
            FieldSpec fieldSpec = FieldSpec.builder(GeneratorUtils.getJavaType(field.getType(), field.getLength()), GeneratorUtils.getFieldName(field.getName()))
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            modelBuilder.addField(fieldSpec);
            switch (flowDirection) {
                case IN:
                    modelBuilder.addMethod(GeneratorUtils.generateDeserializingGetter(field, messageSpec.getBodyOffset(field.getName()), field.getLength()));
                    modelBuilder.addMethod(GeneratorUtils.generateSetter(fieldSpec));
                    break;
                case OUT:
                    modelBuilder.addMethod(GeneratorUtils.generateGetter(fieldSpec));
                    modelBuilder.addMethod(GeneratorUtils.generateSerializingSetter(field, messageSpec.getBodyOffset(field.getName()), field.getLength()));
                    break;
                case BOTH:
                    modelBuilder.addMethod(GeneratorUtils.generateDeserializingGetter(field, messageSpec.getBodyOffset(field.getName()), field.getLength()));
                    modelBuilder.addMethod(GeneratorUtils.generateSerializingSetter(field, messageSpec.getBodyOffset(field.getName()), field.getLength()));
                    break;
            }
        });

        return modelBuilder.build();
    }

    public static TypeSpec generateBaseMessageModel(Spec adaptorSpec, String className) {

        FieldSpec rawMessageData = FieldSpec.builder(ArrayTypeName.of(byte.class), "rawMessageData", Modifier.PROTECTED).build();

        TypeSpec.Builder baseInterfaceBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);

        adaptorSpec.getMessageHeader().getFields().forEach(field -> {
            if(field.getType() == null) {
                LOG.info("{}", field.getName());
            }
            FieldSpec fieldSpec = FieldSpec.builder(GeneratorUtils.getJavaType(field.getType(), field.getLength()), GeneratorUtils.getFieldName(field.getName()))
                    .addModifiers(Modifier.PROTECTED)
                    .build();
            baseInterfaceBuilder.addField(fieldSpec);
            baseInterfaceBuilder.addMethod(GeneratorUtils.generateGetter(fieldSpec));
            baseInterfaceBuilder.addMethod(GeneratorUtils.generateSetter(fieldSpec));
        });

        baseInterfaceBuilder.addField(rawMessageData)
                .addMethod(GeneratorUtils.generateGetter(rawMessageData))
                .addMethod(GeneratorUtils.generateSetter(rawMessageData));

        return baseInterfaceBuilder.build();
    }

    public static List<TypeSpec> generateMessageHandlers(Spec adaptorSpec) {
        TypeSpec.Builder inMsgHandlerBuilder = TypeSpec.classBuilder("IncomingMessageHandler")
                .addModifiers(Modifier.PUBLIC);
        TypeSpec.Builder outMsgHandlerBuilder = TypeSpec.classBuilder("OutgoingMessageHandler")
                .addModifiers(Modifier.PUBLIC);

        Map<String, MessageSpec> messageDictionary =  adaptorSpec.getMessageDictionary();
        messageDictionary.forEach((k, v) -> {
            MethodSpec receiveSpec = GeneratorUtils.generateReceiveHandler(k, PackageManager.getModelPackageName());
            MethodSpec sendSpec = GeneratorUtils.generateSendHandler(k, PackageManager.getModelPackageName());
            switch (v.getFlowDirection()) {
                case IN:
                    inMsgHandlerBuilder.addMethod(receiveSpec);
                    break;
                case OUT:
                    outMsgHandlerBuilder.addMethod(sendSpec);
                    break;
                case BOTH:
                    inMsgHandlerBuilder.addMethod(receiveSpec);
                    outMsgHandlerBuilder.addMethod(sendSpec);
                    break;
            }
        });

        return new ArrayList<>(Arrays.asList(inMsgHandlerBuilder.build(), outMsgHandlerBuilder.build()));
    }

    public static TypeSpec generateMessageFactory(Spec adaptorSpec) {
        TypeSpec.Builder factoryBuilder = TypeSpec.classBuilder("MessageFactory")
                .addModifiers(Modifier.PUBLIC);
        ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(PackageManager.getEnumPackageName(), "MessageType"), "messageType").build();
        ClassName optional = ClassName.get("java.util", "Optional");
        ClassName message = ClassName.get(PackageManager.getModelPackageName(), "Message");
        TypeName optinalMessage = ParameterizedTypeName.get(optional, message);

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        Map<String, MessageSpec> messageDictionary =  adaptorSpec.getMessageDictionary();
        codeBlockBuilder.beginControlFlow("switch(messageType)");
        messageDictionary.forEach((k, v) -> {
            ClassName className = ClassName.get(PackageManager.getModelPackageName(),GeneratorUtils.getClassName(k));
            codeBlockBuilder.add("case $L:\n", k);
            codeBlockBuilder.addStatement("$>return Optional.of(new $T())$<",className);
        });
        codeBlockBuilder.add("default :\n");
        codeBlockBuilder.addStatement("$>return Optional.empty()$<");
        codeBlockBuilder.endControlFlow();

        MethodSpec methodSpec = MethodSpec.methodBuilder("instanceOf")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec)
                .addCode(codeBlockBuilder.indent().build())
                .returns(optinalMessage)
                .build();

        factoryBuilder.addMethod(methodSpec);
        return factoryBuilder.build();
    }
}
