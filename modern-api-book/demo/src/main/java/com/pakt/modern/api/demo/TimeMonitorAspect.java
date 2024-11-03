package com.pakt.modern.api.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeMonitorAspect {
  @Around("@annotation(TimeMonitor)")
  public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("in logtime");
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed();
    long executionTime = System.currentTimeMillis() - start;
    System.out.println(joinPoint.getSignature() + " takes: " + executionTime + " ms");
    return proceed;
  }

}
