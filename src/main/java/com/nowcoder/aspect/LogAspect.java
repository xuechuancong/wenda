package com.nowcoder.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Aspect
@Component
public class LogAspect {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {

        StringBuilder sb = new StringBuilder();
        for (Object arg: joinPoint.getArgs()
             ) {
            sb.append("arg: ").append(arg.toString()).append("|");
        }

        logger.info("before method" + "args: " + sb.toString() );
    }

    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void afterMethod() {
        logger.info("after method " + new Date());
    }
}
