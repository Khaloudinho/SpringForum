package fr.miage.sid.forum.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionProfiler {

  @Around("@annotation(fr.miage.sid.forum.aspects.LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object proceed = joinPoint.proceed();

    long time = System.currentTimeMillis() - start;

    log.info("Execution time of method " + joinPoint.getSignature() + " is: " + time + " ms");
    return proceed;
  }

}
