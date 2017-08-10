package de.maltorpro.shop.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {

  static Logger log = LoggerFactory.getLogger(LoggingAspect.class);

  @Around("execution(* de.maltorpro.shop.service..*(..))")
  public Object profileExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

    long start = System.currentTimeMillis();
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    String apiName = className + "."+ methodName;
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String requestId = UUID.randomUUID().toString();
    log.info("----->>>>>\nREQUESTED_ID: {}\nHOST: {} HttpMethod: {}\nURI: {}\nAPI: {}\nArguments: {}\n",
        requestId,
        request.getHeader("host"),
        request.getMethod(),
        request.getRequestURI(),
        apiName,
        Arrays.toString(joinPoint.getArgs()));

    Object result = joinPoint.proceed();
    long elapsedTime = System.currentTimeMillis() - start;
    log.info("<<<<<-----\nExecution Time: {} ms [REQUESTED_ID: {}] [API: {}]", elapsedTime,requestId,apiName);

    return result;
  }
}