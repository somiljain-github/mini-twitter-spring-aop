package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.security.AccessControlException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(2)
public class AccessControlAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */
	
	@Autowired TweetStatsServiceImpl stats;
	
	@SuppressWarnings({ "removal", "deprecation" })
	@Before("likeMethod()")
	public void accessLikeAdvice(JoinPoint joinpoint) throws Throwable{
		String user = (String)joinpoint.getArgs()[0];
		UUID tweetUUID = (UUID)joinpoint.getArgs()[1];
		
		//check if the tweet exists
		HashMap<UUID, String> tweetSentBy = stats.getTweetSentBy();
		if(!tweetSentBy.containsKey(tweetUUID)) {
			throw new AccessControlException("The given tweet does not exist.");
		}
		
		//check if the user already liked the message
		HashMap<UUID, HashSet<String>> tweetLikedBy = stats.getTweetLikedBy();
		HashSet<String> temp = tweetLikedBy.get(tweetUUID);
		//inital state - if message has not been liked at all and if the user is the first one to like the tweet
		if(temp.contains(user)) {
			throw new AccessControlException("User has already liked the tweet.");
		}
		
		//check if the user is trying to like his own message
		String senderOfTheTweet = tweetSentBy.get(tweetUUID);
		if(user.equals(senderOfTheTweet)) {
			throw new AccessControlException("User is trying to like his own tweet.");
		}
		
		//check if the original tweet/message was shared with the user
		HashMap<UUID, HashSet<String>> messageSharedWith = stats.getTweetSharedWith();
		HashSet<String> usersWhoCanAccessTheMessage = messageSharedWith.get(tweetUUID);
		if(!usersWhoCanAccessTheMessage.contains(user)) {
			throw new AccessControlException("User is trying to like a tweet that was not shared with them");
		}
		
	}
	
	@Before("replyMethod()")
	public void accessReplyAdvice(JoinPoint joinpoint) throws Throwable{
		/** @throws AccessControlException if the current user has not been shared with the original message 
		 * or the current user has blocked the original sender.
		 */
		
		HashSet<String> emptyHashSet = new HashSet<String>();

		String user = (String)joinpoint.getArgs()[0];
		UUID originaltweetUUID = (UUID)joinpoint.getArgs()[1];
		String tweetReply = (String)joinpoint.getArgs()[2];

		
		//if the current user has not been shared with the original message 
		HashMap<UUID, HashSet<String>> tweetSharedWith = stats.getTweetSharedWith();
		HashSet<String> usersWhoCanAccessTheMessage = tweetSharedWith.get(originaltweetUUID);
		
		if(!usersWhoCanAccessTheMessage.contains(user)) {
			throw new AccessControlException("User is trying to reply to a tweet that was not shared with them.");
		}
		
		//the current user has blocked the original sender
		HashMap<UUID, String> tweetSentBy = stats.getTweetSentBy();
		String originalSender = tweetSentBy.get(originaltweetUUID);
		
		HashMap<String, HashSet<String>> userBlockedBy = stats.getUserBlockedBy();
		HashSet<String> usersWhoBlockedOriginalSender = userBlockedBy.getOrDefault(originalSender, emptyHashSet);
		if(usersWhoBlockedOriginalSender.contains(user)) {
			throw new AccessControlException("The current user has blocked the original sender.");
		}
		
	}
	
	
//	========================================PointCuts' Definitions==================================================
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void likeMethod() {
		//function intentionally left empty
	}
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void replyMethod() {
		//function intentionally left empty
	}
	
}
