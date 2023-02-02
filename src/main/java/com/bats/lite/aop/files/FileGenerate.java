package com.bats.lite.aop.files;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.bats.lite.aop.files.FileType.PDF;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileGenerate {

    Class<?> aClass();
    FileType FILE_TYPE() default PDF;

}
