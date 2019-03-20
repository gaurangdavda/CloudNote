package edu.neu.csye6225.spring19.cloudninja.metrics;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import com.google.common.base.Stopwatch;
import com.timgroup.statsd.StatsDClient;

@Aspect
public class MethodProfiler {

	private final StatsDClient statsDClient;

	private static final Logger logger = LogManager.getLogger();

	public MethodProfiler(StatsDClient statsDClient) {
		this.statsDClient = statsDClient;
	}

	@Pointcut("execution(* edu.neu.csye6225.spring19.cloudninja.controller.EntryController.*(..))")
	public void restServiceMethods() {
	}

	@Around("restServiceMethods()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {

		// execute the method, record the result and measure the time
		logger.log(Level.INFO, "Method Entered: " + pjp.getSignature().getName());
		Stopwatch stopwatch = Stopwatch.createStarted();
		Object output = pjp.proceed();
		stopwatch.stop();
		logger.log(Level.INFO, "Method Exited: " + pjp.getSignature().getName());

		// send the recorded time to statsd
		String key = String.format("%s.%s", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
		statsDClient.recordExecutionTime(key, stopwatch.elapsed(TimeUnit.MILLISECONDS));
		//System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
		// return the recorded result
		return output;
	}

	
	@Before("restServiceMethods()")
	public void countEndpointCall(JoinPoint joinPoint) {
		System.out.println(joinPoint.getSignature().getName());
		// Calling the statsDClient and incrementing count by 1 for respective endpoint.
		// joinPoint.getSignature().getName() returns the name of the method for which
		// this AOP method is called
		statsDClient.increment(joinPoint.getSignature().getName());
	}

	// Method to log exceptions occurred in application
	@AfterThrowing(value = "restServiceMethods()",throwing = "e")
	public void logExceptions(JoinPoint joinPoint, Throwable e) {
		logger.log(Level.ERROR,"Exception occurred in: " + joinPoint.getSignature().getName() + "Exception message: " + e.getMessage());
	}

}
