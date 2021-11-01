package com.filesfromyou.logprocessor.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class BeforeAdvice {
    Logger log = LoggerFactory.getLogger(BeforeAdvice.class);

    @Before("execution(* com.filesfromyou.*.*.*.*(..) )")
    public void myBeforeLogger(JoinPoint joinPoint) throws JsonProcessingException {
        try {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().toString();
            Object[] array = joinPoint.getArgs();
            ObjectMapper mapper = new ObjectMapper();
            log.info("Method invoked " + className + ": " + methodName + "()" + " arguments: " + mapper.writeValueAsString(array));
        } catch (Throwable ignored) {}
    }
}
