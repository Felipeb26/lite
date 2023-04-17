package com.bats.lite.aop.service;

import org.springframework.stereotype.Service;

@Service
public interface FilesToGenerate {

    Object createPDF(Class<?> aClass, Object object, String watermark);

    Object createCSV(Class<?> aClass, Object object);

    Object createEXCEL(Class<?> aClass, Object object);
}
