package com.daiqi.aspect;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.daiqi.controller..*.*(..))")
    public void webLog() {}

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes == null ? null : attributes.getRequest();

        log.info("======= 开始请求 =======");
        if (request != null) {
            log.info("URL    : {}", request.getRequestURL().toString());
            log.info("HTTP方法: {}", request.getMethod());
        } else {
            log.info("URL    : N/A");
            log.info("HTTP方法: N/A");
        }
        log.info("类方法  : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("请求参数: {}", Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        log.info("返回结果: {}", result);
        log.info("执行耗时: {} ms", System.currentTimeMillis() - startTime);
        log.info("======= 结束请求 =======");

        return result;
    }
}
