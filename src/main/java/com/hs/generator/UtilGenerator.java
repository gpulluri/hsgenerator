package com.hs.generator;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;


/**
 * Created by gpulluri on 5/20/17.
 */
public class UtilGenerator {

    public static TypeSpec generateMessageUtils() {

        MethodSpec intToBytes = MethodSpec.methodBuilder("integerToBytes")
                .addParameter(int.class,"input")
                .addParameter(ArrayTypeName.of(byte.class),"target")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .beginControlFlow("for (int i = length-1; i >= 0; i--)")
                .addStatement("target[offset+i] = (byte)(input & 0xFF)")
                .addStatement("input >>= 8")
                .endControlFlow()
                .addStatement("return target")
                .returns(ArrayTypeName.of(byte.class))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec bytesToInt = MethodSpec.methodBuilder("bytesToInteger")
                .addParameter(ArrayTypeName.of(byte.class),"input")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .addStatement("int result = 0")
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("result <<= 8")
                .addStatement("result |= (input[offset+i] & 0xFF)")
                .endControlFlow()
                .addStatement("return result")
                .returns(int.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec shortToBytes = MethodSpec.methodBuilder("shortToBytes")
                .addParameter(short.class,"input")
                .addParameter(ArrayTypeName.of(byte.class),"target")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .beginControlFlow("for (int i = length-1; i >= 0; i--)")
                .addStatement("target[offset+i] = (byte)(input & 0xFF)")
                .addStatement("input >>= 8")
                .endControlFlow()
                .addStatement("return target")
                .returns(ArrayTypeName.of(byte.class))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec bytesToShort = MethodSpec.methodBuilder("bytesToShort")
                .addParameter(ArrayTypeName.of(byte.class),"input")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .addStatement("short result = 0")
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("result <<= 8")
                .addStatement("result |= (input[offset+i] & 0xFF)")
                .endControlFlow()
                .addStatement("return result")
                .returns(short.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec longToBytes = MethodSpec.methodBuilder("longToBytes")
                .addParameter(long.class,"input")
                .addParameter(ArrayTypeName.of(byte.class),"target")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .beginControlFlow("for (int i = length-1; i >= 0; i--)")
                .addStatement("target[offset+i] = (byte)(input & 0xFF)")
                .addStatement("input >>= 8")
                .endControlFlow()
                .addStatement("return target")
                .returns(ArrayTypeName.of(byte.class))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec bytesToLong = MethodSpec.methodBuilder("bytesToLong")
                .addParameter(ArrayTypeName.of(byte.class),"input")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .addStatement("long result = 0")
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("result <<= 8")
                .addStatement("result |= (input[offset+i] & 0xFF)")
                .endControlFlow()
                .addStatement("return result")
                .returns(long.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec characterToBytes = MethodSpec.methodBuilder("characterToBytes")
                .addParameter(char.class,"input")
                .addParameter(ArrayTypeName.of(byte.class),"target")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .beginControlFlow("for (int i = length-1; i >= 0; i--)")
                .addStatement("target[offset+i] = (byte)(input & 0xFF)")
                .addStatement("input >>= 8")
                .endControlFlow()
                .addStatement("return target")
                .returns(ArrayTypeName.of(byte.class))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec bytesToCharacter = MethodSpec.methodBuilder("bytesToCharacter")
                .addParameter(ArrayTypeName.of(byte.class),"input")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .addStatement("char result = '\0'")
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("result <<= 8")
                .addStatement("result |= (input[offset+i] & 0xFF)")
                .endControlFlow()
                .addStatement("return result")
                .returns(char.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec stringToBytes = MethodSpec.methodBuilder("stringToBytes")
                .addParameter(String.class,"input")
                .addParameter(ArrayTypeName.of(byte.class),"target")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .addStatement("int strLen = input.length()")
                .beginControlFlow("for (int i = 0; i < strLen; i++)")
                .addStatement("target[offset+i] = (byte)input.charAt(i)")
                .endControlFlow()
                .beginControlFlow("for(int j = strLen; j<length; j++)")
                .addStatement("target[j] = $T.FIELD_PADDING", ClassName.get(PackageManager.getBasePackageName(), "Constants"))
                .endControlFlow()
                .addStatement("return target")
                .returns(ArrayTypeName.of(byte.class))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec bytesToString = MethodSpec.methodBuilder("bytesToString")
                .addParameter(ArrayTypeName.of(byte.class),"input")
                .addParameter(int.class,"offset")
                .addParameter(int.class,"length")
                .addStatement("char[] result = new char[input.length]")
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("result[i] = (char)input[offset+i]")
                .endControlFlow()
                .addStatement("return new String(result)")
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();


        TypeSpec.Builder utilBuilder = TypeSpec.classBuilder("MessageUtils")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(intToBytes)
                .addMethod(bytesToInt)
                .addMethod(shortToBytes)
                .addMethod(bytesToShort)
                .addMethod(longToBytes)
                .addMethod(bytesToLong)
                .addMethod(characterToBytes)
                .addMethod(bytesToCharacter)
                .addMethod(stringToBytes)
                .addMethod(bytesToString);

        return utilBuilder.build();
    }
}
