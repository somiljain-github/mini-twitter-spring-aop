package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(1)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	
	@Autowired TweetStatsServiceImpl stats;
	
	@Before("followMethod()")
	public void validateFollowAdvice(JoinPoint joinpoint) throws Throwable{
		String follower = (String)joinpoint.getArgs()[0]; 
		String followee = (String)joinpoint.getArgs()[1];
		
		if(follower==null || follower=="" || followee==null || followee=="") {
			throw new IllegalArgumentException("Both the arguments for the follow method are required. They cannot be null or empty");
		}
		
		if(follower==followee) {
			throw new IllegalArgumentException("An user cannot follow themself. Both the arguments of the follow function cannot be same.");
		}
	}
	
	@Before("tweetMethod()")
	public void validateTweetAdvice(JoinPoint joinpoint) throws Throwable{
		String user = (String)joinpoint.getArgs()[0]; 
		String tweet = (String)joinpoint.getArgs()[1];
		
		if(user==null || user=="" || tweet==null || tweet=="") {
			throw new IllegalArgumentException("Both the arguments  for the follow method are required. They cannot be null or empty");
		}
		
		if(tweet.length()>140) {
			throw new IllegalArgumentException("The length of the tweet is greater than 140 characters");
		}	
	}
	
	@Before("replyMethod()")
	public void validateReplyAdvice(JoinPoint joinpoint) throws Throwable {
		String user = (String)joinpoint.getArgs()[0];
		UUID originaltweetUUID = (UUID)joinpoint.getArgs()[1];
		String tweetReply = (String)joinpoint.getArgs()[2];
		UUID emptyUUID = UUID.fromString( "00000000-0000-0000-0000-000000000000");
		
		if(user==null || user=="" || originaltweetUUID==null || originaltweetUUID==emptyUUID || tweetReply==null || tweetReply=="") {
			throw new IllegalArgumentException("All three arguments are required. They cannot be null or empty");
		}
		
		
		if(tweetReply.length()>140) {
			throw new IllegalArgumentException("The length of the tweet reply is greater than 140 characters");
		}
		
		
		//when a user attempts to directly reply to a message by themselves
		HashMap<UUID, String> tweetSentBy = stats.getTweetSentBy();
		String senderOfTheOriginalTweet = tweetSentBy.getOrDefault(originaltweetUUID, null);
		if(senderOfTheOriginalTweet == user) {
			throw new IllegalArgumentException("A user cannot reply to their own tweet.");
		}
		
		if(senderOfTheOriginalTweet == null) {
			throw new IllegalArgumentException("The given UUID/tweet does not exist");
		}
		
	}
	
	@Before("blockMethod()")
	public void validateBlockAdvice(JoinPoint joinpoint) throws Throwable {
		String blocker = (String)joinpoint.getArgs()[0]; 
		String blockee = (String)joinpoint.getArgs()[1];
		HashSet<String> emptyHashSet = new HashSet<String>();
		
		//check if any parameters are null or empty
		if(blocker==null || blocker=="" || blockee==null || blockee=="") {
			throw new IllegalArgumentException("Both the arguments in the block method are required. They cannot be null or empty");
		}
		
		if(blocker==blockee) {
			throw new IllegalArgumentException("The user cannot block themself");
		}
	}
	
	@Before("likeMethod()")
	public void validateLikeAdvice(JoinPoint joinpoint) throws Throwable{
		String user = (String)joinpoint.getArgs()[0];
		UUID tweetUUID = (UUID)joinpoint.getArgs()[1];
		
		
//		check if any of the sent arguments is null or empty
		if(user==null || user=="" || tweetUUID==null) {
			throw new IllegalArgumentException("Both the arguments in the like method are required. They cannot be null or empty");
		}
	}
	
	
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void followMethod() {
		//function intentionally left empty
	}
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void tweetMethod() {
		//function intentionally left empty
	}
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void likeMethod() {
		//function intentionally left empty
	}
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void replyMethod() {
		//function intentionally left empty
	}
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void blockMethod() {
		//function intentionally left empty
	}
}
