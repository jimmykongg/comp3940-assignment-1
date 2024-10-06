package com.quizapp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class Logging {

    @Before("execution(* *.doPost(..)) ")
    public void beforeDoPostCall(JoinPoint joinPoint) {
        System.out.println("Aspect: About to call doPost");
    }

    @After("execution(* *.doPost(..)) ")
        public void afterDoPostCall(JoinPoint joinPoint) {
        System.out.println("Aspect: doPost completed");
    }

    @Before("execution(* *.doGet(..)) ")
    public void beforeDoGetCall(JoinPoint joinPoint) {
        System.out.println("Aspect: About to call doGet");
    }

    @After("execution(* *.doGet(..)) ")
    public void afterDoGetCall(JoinPoint joinPoint) {
        System.out.println("Aspect: doGet completed");
    }
}