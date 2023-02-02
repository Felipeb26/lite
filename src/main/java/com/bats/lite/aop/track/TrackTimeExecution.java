package com.bats.lite.aop.track;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class TrackTimeExecution {

    @Around("@annotation(com.bats.lite.aop.track.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Starting method: {}", getMethodName(joinPoint.getSignature()));
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Method: {} have taken {} seconds to finish!", getMethodName(joinPoint.getSignature()), milisToSeconds(startTime, endTime));
        return obj;
    }

    private String getMethodName(Signature signature) {
        return signature.getName();
    }

    private double milisToSeconds(long l1, long l2) {
        return (double) (l2 - l1) / 1000.0;
    }

}
