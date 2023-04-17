package com.bats.lite.aop.files;

import com.bats.lite.aop.service.FilesToGenerate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Aspect
@Component
public class FileGenerationExecution {

    @Autowired
    private FilesToGenerate filesToGenerate;

    private Class<?> findClass(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    private FileType discoverFileType(Object o, FileType fileType) {
        if (o instanceof ResponseEntity) {
            HttpHeaders headers = ((ResponseEntity<?>) o).getHeaders();
            var header = headers.get("file-type");
            if(isNull(header))
                return fileType;
            Optional<String> optional = header.stream().findFirst();
            if (optional.isPresent()) {
                for (FileType type : FileType.values()) {
                    if (type.getFileType().equalsIgnoreCase(optional.get()))
                        return type;
                }
            }
        }
        return fileType;
    }

    @Around("@annotation(com.bats.lite.aop.files.FileGenerate)")
    public Object object(ProceedingJoinPoint joinPoint) throws Throwable {
        var object = joinPoint.proceed();

        Class<?> aClass = findClass(joinPoint);
        Method[] methods = aClass.getDeclaredMethods();
        for (var method : methods) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            if (method.equals(signature.getMethod())) {
                FileGenerate generate = method.getAnnotation(FileGenerate.class);
                object = fileChoose(discoverFileType(object, generate.FILE_TYPE()), generate.ClassName(), object, generate.watermark());
                break;
            }
        }
        return object;
    }

    private Object fileChoose(FileType fileType, Class<?> aClass, Object object, String watermark) {
        switch (fileType) {
            case CSV:
                return filesToGenerate.createCSV(aClass, object);
            case PDF:
                return filesToGenerate.createPDF(aClass, object, watermark);
            case EXCEL:
                return filesToGenerate.createEXCEL(aClass, object);
            default:
                return filesToGenerate.createPDF(aClass, object, watermark);
        }
    }


}
