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

import static java.util.Objects.nonNull;

@Slf4j
@Aspect
@Component
public class FileGenerationExecution {

    private final String header_file = "file-type";
    @Autowired
    private FilesToGenerate filesToGenerate;

    private Class<?> findClass(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    private FileType discoverFileType(Object o, FileType fileType) {
        if (o instanceof ResponseEntity) {
            HttpHeaders headers = ((ResponseEntity<?>) o).getHeaders();
            for (FileType type : FileType.values()) {
                if (nonNull(headers.get(header_file))) {
                    Optional<String> optional = headers.get(header_file).stream().findFirst();
                    if (optional.isPresent()) {
                        if (type.getFileType().equalsIgnoreCase(optional.get())) {
                            return type;
                        }
                    }
                } else {
                    break;
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
                object = fileChoose(generate.FILE_TYPE(), generate.ClassName(), object);
                break;
            }
        }
        return object;
    }

    private Object fileChoose(FileType fileType, Class<?> aClass, Object object) {
        switch (fileType) {
            case CSV:
                return filesToGenerate.createCSV(aClass, object);
            case PDF:
                return filesToGenerate.createPDF(aClass, object);
            case EXCEL:
                return filesToGenerate.createEXCEL(aClass, object);
            default:
                return filesToGenerate.createPDF(aClass, object);
        }
    }


}
