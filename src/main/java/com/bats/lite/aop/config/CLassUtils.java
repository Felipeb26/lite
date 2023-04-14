package com.bats.lite.aop.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CLassUtils {

    public static String getTrueClassName(Class<?> aClass) {
        String fullName = aClass.getName();
        int index = fullName.lastIndexOf(".");
        return index > 0 ? fullName.substring(index) : fullName;
    }
}
