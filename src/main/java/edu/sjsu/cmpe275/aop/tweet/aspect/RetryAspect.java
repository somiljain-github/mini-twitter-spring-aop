package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
@Order(0)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */
	
	private static Integer maxRetry = 3;
	private static Integer retryCurrentCount = 0; //start with zero and increment for each retry. reset at the end of operation.
		
	@Around("allTweetServiceFunctions()")
	public Object retryAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Object returnObject = null;
		while(RetryAspect.retryCurrentCount<=RetryAspect.maxRetry) {
			try {
				returnObject = joinPoint.proceed();
				return returnObject;
			} catch (IOException e) {
				RetryAspect.retryCurrentCount += 1;
				System.out.println("The error thrown is: " + e);
				if(RetryAspect.retryCurrentCount>RetryAspect.maxRetry) {
					RetryAspect.retryCurrentCount = 0;
					System.out.println("Tried 3 times. Throwing a final IOException error and exiting the retryAdvice");
					throw new IOException();
				} else {
					System.out.println("Trying again for attempt number: "+RetryAspect.retryCurrentCount);
				}
			}
		}
		return returnObject;
	}
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	void allTweetServiceFunctions(){
		//function intentionally left empty
	};
}
