package by.roboworld.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Logging all actions on beans with the indication of execution time.
 */
@Component
@Aspect
public class LogAspect {

    @Around("execution(* by.roboworld..*.*(..))")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object returnValue = joinPoint.proceed();

        stopWatch.stop();

        StringBuilder message = new StringBuilder();
        message.append(joinPoint.getTarget().getClass().getName());
        message.append('.');
        message.append(joinPoint.getSignature().getName());
        message.append('(');
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            message.append(arg);
            message.append(", ");
        }
        if (args.length > 0) {
            message.deleteCharAt(message.length() - 2);
        }
        message.append(')');
        message.append(" execution time ");
        message.append(stopWatch.getTotalTimeMillis());
        message.append(" ms");
        logger.trace(message.toString());
        return returnValue;
    }

}
