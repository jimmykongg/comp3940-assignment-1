package com.quizapp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class Logging {
    private static final Logger logger = LoggerFactory.getLogger(Logging.class);

        @Before("execution(public void com.quizapp.authentication.LoginServlet.doPost(..))")
    public void beforeDoPostCall(JoinPoint joinPoint) {
        logger.info("Aspect: About to call doPost for login");
        System.out.println("Aspect: About to call doPost for login");
    }

    @After("execution(public void" +
            " com.quizapp.authentication.LoginServlet.doPost(..))")
    public void afterDoPostCall(JoinPoint joinPoint) {
        logger.info("Aspect: doPost completed for login");
        System.out.println("Aspect: doPost completed for login");
    }

//    @Before("execution(public * com.quizapp.controller.RouteServlet(..))")
//    public void beforeHttpMethodCall(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        logger.info("Aspect: About to call " + methodName + " method");
//        System.out.println("Aspect: About to call " + methodName + " method");
//    }
//
//    @After("execution(public * com.quizapp.authentication.*(..))")
//    public void afterHttpMethodCall(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        logger.info("Aspect: " + methodName + " method completed");
//        System.out.println("Aspect: " + methodName + " method completed");
//    }

//    @Around("execution(* com.quizapp.authentication.*(..))")
//    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
//        // Custom logic before the method execution
//        logger.info("Before method execution");
//
//        // Proceed with the actual method execution
//        Object result = joinPoint.proceed();
//
//        // Custom logic after the method execution
//        logger.info("After method execution");
//
//        return result;
//    }

}