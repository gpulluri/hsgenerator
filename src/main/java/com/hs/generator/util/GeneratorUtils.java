package com.hs.generator.util;

import com.hs.generator.PackageManager;
import com.hs.generator.model.Field;
import com.hs.generator.model.FieldType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.apache.commons.lang3.text.WordUtils;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

/**
 * Created by gpulluri on 5/18/17.
 */
public class GeneratorUtils {

    public static String getPackageName(String value) {
        return value.toLowerCase().replace("-", ".").replace("_", ".");
    }

    public static String getClassName(String value) {
        value = value.toLowerCase().replace("-", " ").replace("_", " ");
        return WordUtils.capitalize(value).replace(" ", "");
    }

    public static String getMethodName(String value) {
        value = value.toLowerCase().replace("-", " ").replace("_", " ");
        return WordUtils.uncapitalize(WordUtils.capitalize(value).replace(" ", ""));
    }

    public static String getFieldName(String value) {
        value = value.toLowerCase().replace("-", " ").replace("_", " ");
        return WordUtils.uncapitalize(WordUtils.capitalize(value).replace(" ", ""));
    }

    public static MethodSpec generateGetter(FieldSpec fieldSpec) {
        MethodSpec methodSpec = MethodSpec.methodBuilder("get"+WordUtils.capitalize(fieldSpec.name))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$L",fieldSpec.name)
                .returns(fieldSpec.type)
                .build();

        return methodSpec;
    }

    public static MethodSpec generateDeserializingGetter(Field field, int offset, int length) {
        ClassName dataUtils = ClassName.get(PackageManager.getUtilPackageName(), "MessageUtils");
        String fieldName = GeneratorUtils.getFieldName(field.getName());
        Type fieldType = getJavaType(field.getType(), field.getLength());
        String fieldTypeString = getJavaTypeString(field.getType(), field.getLength());
        FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName)
                .addModifiers(Modifier.PRIVATE)
                .build();
        MethodSpec methodSpec = MethodSpec.methodBuilder("get"+WordUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if(this.$L == null)", fieldSpec.name)
                .addStatement("this.$L = $T.bytesTo$L(rawMessageData, $L, $L)", fieldSpec.name, dataUtils,fieldTypeString, offset, length)
                .endControlFlow()
                .addStatement("return this.$L",fieldSpec.name)
                .returns(fieldSpec.type)
                .build();

        return methodSpec;
    }

    public static MethodSpec generateSerializingSetter(Field field, int offset, Integer length) {
        ClassName dataUtils = ClassName.get(PackageManager.getUtilPackageName(), "MessageUtils");
        String fieldName = GeneratorUtils.getFieldName(field.getName());
        Type fieldType = getJavaType(field.getType(), field.getLength());
        String fieldTypeString = getJavaTypeString(field.getType(), field.getLength());
        FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName)
                .addModifiers(Modifier.PRIVATE)
                .build();
        MethodSpec methodSpec = MethodSpec.methodBuilder("set"+WordUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(fieldSpec.type, "input")
                .addStatement("this.$L = input", fieldName)
                .addStatement("$T.$LToBytes(input, rawMessageData, $L, $L)", dataUtils,fieldTypeString.toLowerCase(), offset, length)
                .build();

        return methodSpec;
    }

    public static MethodSpec generateSetter(FieldSpec fieldSpec) {
        MethodSpec methodSpec = MethodSpec.methodBuilder("set"+WordUtils.capitalize(fieldSpec.name))
                .addParameter(fieldSpec.type, fieldSpec.name)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$L = $L",fieldSpec.name, fieldSpec.name)
                .returns(void.class)
                .build();

        return methodSpec;
    }

    public static MethodSpec generateReceiveHandler(String messageType, String modelPackageName) {

        MethodSpec methodSpec = MethodSpec.methodBuilder("on"+WordUtils.capitalize(getMethodName(messageType)))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(modelPackageName, getClassName(messageType)), getFieldName(messageType))
                .build();

        return methodSpec;
    }

    public static MethodSpec generateSendHandler(String messageType, String modelPackageName) {
        ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(modelPackageName, getClassName(messageType)), getFieldName(messageType)).build();
        MethodSpec methodSpec = MethodSpec.methodBuilder("send"+WordUtils.capitalize(getMethodName(messageType)))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec)
                .build();

        return methodSpec;
    }

    public static Type getJavaType(FieldType fieldType, Integer length) {
        switch (fieldType) {
            case ALPHA:
                if(length == 1) return Character.class;
                else return String.class;
            case SHORT:
                return Short.class;
            case INTEGER:
                return Integer.class;
            case LONG:
                return Long.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            default:
                return byte.class;
        }
    }

    public static String getJavaTypeString(FieldType fieldType, Integer length) {
        switch (fieldType) {
            case ALPHA:
                if(length == 1) return "Character";
                else return "String";
            case SHORT:
                return "Short";
            case INTEGER:
                return "Integer";
            case LONG:
                return "Long";
            case DOUBLE:
                return "Double";
            case FLOAT:
                return "Float";
            default:
                return "byte";
        }
    }
}
