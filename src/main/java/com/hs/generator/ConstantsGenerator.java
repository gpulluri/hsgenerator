package com.hs.generator;

import com.hs.generator.model.Spec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by gpulluri on 5/21/17.
 */
public class ConstantsGenerator {

    public static TypeSpec generateConstants(Spec adaptorSpec) {
        FieldSpec messageStart = FieldSpec.builder(TypeName.BYTE, "MESSAGE_START", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", adaptorSpec.getMessageStart().getValue())
                .build();
        FieldSpec messageEnd = FieldSpec.builder(TypeName.BYTE, "MESSAGE_END", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", adaptorSpec.getMessageEnd().getValue())
                .build();
        FieldSpec fieldPadding = FieldSpec.builder(TypeName.BYTE, "FIELD_PADDING", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", adaptorSpec.getFieldPadding().getValue())
                .build();

        TypeSpec constants = TypeSpec.classBuilder("Constants")
                .addModifiers(Modifier.PUBLIC)
                .addField(messageStart)
                .addField(messageEnd)
                .addField(fieldPadding)
                .build();

        return constants;
    }
}
