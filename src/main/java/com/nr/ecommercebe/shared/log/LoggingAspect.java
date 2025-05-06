package com.nr.ecommercebe.shared.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
class LoggingAspect {

    // Pointcut for services and controllers
    @Pointcut("within(@org.springframework.stereotype.Service *) || within(@org.springframework.stereotype.Controller *)")
    public void applicationBeans() {}

    @Around("applicationBeans()")
    public Object logExecutionDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Object[] args = joinPoint.getArgs();
        String shortArgs = Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";
                    String str = arg.toString();
                    return str.length() > 50 ? str.substring(0, 50) + "..." : str;
                })
                .reduce((a, b) -> a + ", " + b).orElse("");

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("[{}.{}] executed in {} ms with args [{}]", className, methodName, duration, shortArgs);
            return result;
        } catch (Exception ex) {
            log.error("[{}.{}] threw exception: {}", className, methodName, ex.getMessage());
            throw ex;
        }
    }
}