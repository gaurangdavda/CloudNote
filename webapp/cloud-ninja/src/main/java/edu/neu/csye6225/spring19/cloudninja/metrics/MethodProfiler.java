package edu.neu.csye6225.spring19.cloudninja.metrics;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.google.common.base.Stopwatch;
import com.timgroup.statsd.StatsDClient;

@Aspect
public class MethodProfiler {

	private final StatsDClient statsDClient;

	public MethodProfiler(StatsDClient statsDClient) {
		this.statsDClient = statsDClient;
	}

	@Pointcut("execution(* edu.neu.csye6225.spring19.cloudninja.controller.EntryController.*(..))")
	public void restServiceMethods() {
	}

	@Around("restServiceMethods()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {

		// execute the method, record the result and measure the time
		Stopwatch stopwatch = Stopwatch.createStarted();
		Object output = pjp.proceed();
		stopwatch.stop();

		// send the recorded time to statsd
		String key = String.format("%s.%s", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
		statsDClient.recordExecutionTime(key, stopwatch.elapsed(TimeUnit.MILLISECONDS));
		// System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
		// return the recorded result
		return output;

	}

	@Before("restServiceMethods()")
	public void countEndpointCall(JoinPoint joinPoint) {

		System.out.println(joinPoint.getSignature().getName());
		// Calling the statsDClient and incremeting count by 1 for respective endpoint.
		// joinPoint.getSignature().getName() returns the name of the method for which
		// this AOP method is called
		statsDClient.increment(joinPoint.getSignature().getName());
	}

}
