package com.hs.generator;

import com.hs.generator.model.Spec;
import com.hs.generator.model.SpecParser;
import com.hs.generator.util.GeneratorUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by gpulluri on 5/4/17.
 */
public class GeneratorMain {

    private static final Logger LOG = LoggerFactory.getLogger(GeneratorMain.class);

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Missing required arguments: argument 1 -> file path - json spec,  argument 2-> output directory");
            System.exit(-1);
        }
        String specPath = args[0];
        String outputPath = args[1];
        GeneratorMain generator = new GeneratorMain();
        try {
            //generator.generateAdaptorFromSpec("/spec/fxt-ouch.json", outputPath);
            generator.generateAdaptorFromSpec(specPath, outputPath);
        } catch (IOException e) {

            LOG.error("Exception writing to filesystem");
        }
    }

    public void generateAdaptorFromSpec(String specPath, String outputPath) throws IOException {
        SpecParser parser = new SpecParser();
        Optional<Spec> spec = parser.parseFile(specPath);
        if(!spec.isPresent()) {
            LOG.error("Spec not parseable");
            System.exit(-1);
        }

        Spec adaptorSpec = spec.get();
        LOG.info("Message count : {}", adaptorSpec.getMessageDictionary().size());

        String basePackageName = "com.hs.generator."+GeneratorUtils.getPackageName(adaptorSpec.getVenue());
        String modelPackageName = basePackageName+".model";
        String enumPackageName = basePackageName+".enums";
        String ioPackageName = basePackageName+".io";
        String logicPackageName = basePackageName+".logic";
        String utilPackageName = basePackageName+".util";

        PackageManager.setBasePackageName(basePackageName);
        PackageManager.setModelPackageName(modelPackageName);
        PackageManager.setEnumPackageName(enumPackageName);
        PackageManager.setIoPackageName(ioPackageName);
        PackageManager.setLogicPackageName(logicPackageName);
        PackageManager.setUtilPackageName(utilPackageName);

        Map<String, List<TypeSpec>> classDefinitions = new HashMap<>();

        List<TypeSpec> constantDefs = new ArrayList<>();
        constantDefs.add(ConstantsGenerator.generateConstants(adaptorSpec));
        classDefinitions.put(basePackageName, constantDefs);

        List<TypeSpec> enumDefs = new ArrayList<>();
        enumDefs.add(ModelGenerator.generateMessageTypeEnum(adaptorSpec));
        classDefinitions.put(enumPackageName, enumDefs);

        String messageBaseClassName = "Message";
        TypeSpec messageBaseClass = ModelGenerator.generateBaseMessageModel(adaptorSpec, messageBaseClassName);
        ClassName superClass = ClassName.get(modelPackageName, messageBaseClassName);
        List<TypeSpec> modelDefs = new ArrayList<>();
        modelDefs.add(messageBaseClass);
        modelDefs.addAll(ModelGenerator.generateMessageModel(adaptorSpec, superClass));
        classDefinitions.put(modelPackageName, modelDefs);

        TypeSpec connector = IOGenerator.generateConnector();
        TypeSpec decoder = IOGenerator.generateMessageDecoder();
        TypeSpec encoder = IOGenerator.generateMessageEncoder();

        List<TypeSpec> ioDefs = new ArrayList<>(Arrays.asList(decoder, encoder, connector));
        List<TypeSpec> handlerDefs = ModelGenerator.generateMessageHandlers(adaptorSpec);
        ioDefs.addAll(handlerDefs);
        classDefinitions.put(ioPackageName, ioDefs);

        TypeSpec messageFactory = ModelGenerator.generateMessageFactory(adaptorSpec);
        List<TypeSpec> logicDefs = new ArrayList<>(Arrays.asList(messageFactory));
        classDefinitions.put(logicPackageName, logicDefs);

        TypeSpec messageUtils = UtilGenerator.generateMessageUtils();
        List<TypeSpec> utilDefs = new ArrayList<>(Arrays.asList(messageUtils));
        classDefinitions.put(utilPackageName, utilDefs);

        for (Map.Entry<String, List<TypeSpec>> entry : classDefinitions.entrySet()) {
            for (TypeSpec classDefinition : entry.getValue()) {
                JavaFile codeFile = JavaFile.builder(entry.getKey(), classDefinition).skipJavaLangImports(true).build();
                File path = new File(outputPath);
                codeFile.writeTo(path);
            }
        }
    }
}
