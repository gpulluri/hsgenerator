package com.hs.generator;



/**
 * Created by gpulluri on 5/21/17.
 */
public class PackageManager {
    public static String basePackageName;
    public static String modelPackageName;
    public static String enumPackageName;
    public static String ioPackageName;
    public static String logicPackageName;
    public static String utilPackageName;

    public static String getBasePackageName() {
        return basePackageName;
    }

    public static void setBasePackageName(String basePackageName) {
        PackageManager.basePackageName = basePackageName;
    }

    public static String getModelPackageName() {
        return modelPackageName;
    }

    public static void setModelPackageName(String modelPackageName) {
        PackageManager.modelPackageName = modelPackageName;
    }

    public static String getEnumPackageName() {
        return enumPackageName;
    }

    public static void setEnumPackageName(String enumPackageName) {
        PackageManager.enumPackageName = enumPackageName;
    }

    public static String getIoPackageName() {
        return ioPackageName;
    }

    public static void setIoPackageName(String ioPackageName) {
        PackageManager.ioPackageName = ioPackageName;
    }

    public static String getLogicPackageName() {
        return logicPackageName;
    }

    public static void setLogicPackageName(String logicPackageName) {
        PackageManager.logicPackageName = logicPackageName;
    }

    public static String getUtilPackageName() {
        return utilPackageName;
    }

    public static void setUtilPackageName(String utilPackageName) {
        PackageManager.utilPackageName = utilPackageName;
    }
}
