package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(3)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired TweetStatsServiceImpl stats;
		
	@AfterReturning(pointcut = "afterTweet()", returning = "tweetUUID")
	public void afterTweetAdvice(JoinPoint joinpoint, UUID tweetUUID) {
		String user = (String)joinpoint.getArgs()[0];
		String tweet = (String)joinpoint.getArgs()[1];
		
		UUID emptyUUID = UUID.fromString( "00000000-0000-0000-0000-000000000000");
		HashSet<String> emptyHashSet = new HashSet<String>();
		
		HashMap<UUID, String> tweetMessage = stats.getTweetMessage();
		tweetMessage.put(tweetUUID, tweet);
		stats.setTweetMessage(tweetMessage);
		
		HashMap<UUID, String> tweetSentBy = stats.getTweetSentBy();
		tweetSentBy.put(tweetUUID, user);
		stats.setTweetSentBy(tweetSentBy);
		
		HashMap<UUID, Integer> tweetThreadDepth = stats.getTweetThreadDepth();
		tweetThreadDepth.put(tweetUUID, 1);
		System.out.println("inside aftertweet and the messageDepth is " + tweetThreadDepth);
		stats.setTweetThreadDepth(tweetThreadDepth);
		
		HashMap<UUID, Integer> tweetLikes = stats.getTweetLikes();
		tweetLikes.put(tweetUUID, 0);
		stats.setTweetLikes(tweetLikes);
		
		HashMap<UUID, HashSet<String>>  tweetLikedBy = stats.getTweetLikedBy();
		tweetLikedBy.put(tweetUUID, emptyHashSet);
		stats.setTweetLikedBy(tweetLikedBy);
		
		HashMap<UUID, HashSet<String>> messageSharedWith = stats.getTweetSharedWith();
		HashMap<String, HashSet<String>> userFollowers = stats.getUserFollowers();
		messageSharedWith.put(tweetUUID, userFollowers.getOrDefault(user, emptyHashSet));
		stats.setTweetSharedWith(messageSharedWith);
		
		HashMap<UUID, UUID> tweetIsAReplyTo = stats.getTweetIsAReplyTo();
		tweetIsAReplyTo.put(tweetUUID, emptyUUID);
		stats.setTweetIsAReplyTo(tweetIsAReplyTo);
		System.out.println();
		System.out.println();
	}
	
	
	
	@AfterReturning(pointcut = "afterReply()", returning = "tweetReplyUUID")
	public void afterReplyAdvice(JoinPoint joinpoint, UUID tweetReplyUUID) {
		String user = (String)joinpoint.getArgs()[0];
		UUID originaltweet = (UUID)joinpoint.getArgs()[1];
		String tweetReply = (String)joinpoint.getArgs()[2];
		
		UUID emptyUUID = UUID.fromString( "00000000-0000-0000-0000-000000000000" );
		HashSet<String> emptyHashSet = new HashSet<String>();
		
		HashMap<UUID, String> tweetMessage = stats.getTweetMessage();
		tweetMessage.put(tweetReplyUUID, tweetReply);
		stats.setTweetMessage(tweetMessage);
		
		HashMap<UUID, String> tweetSentBy = stats.getTweetSentBy();
		tweetSentBy.put(tweetReplyUUID, user);
		stats.setTweetSentBy(tweetSentBy);
		
		HashMap<UUID, Integer> tweetThreadDepth = stats.getTweetThreadDepth();
		tweetThreadDepth.put(tweetReplyUUID, tweetThreadDepth.get(originaltweet)+1);
		System.out.println("inside afterReplyAdvice and the messageDepth is " + tweetThreadDepth);
		stats.setTweetThreadDepth(tweetThreadDepth);
		
		HashMap<UUID, Integer> tweetLikes = stats.getTweetLikes();
		tweetLikes.put(tweetReplyUUID, 0);
		stats.setTweetLikes(tweetLikes);
		
		HashMap<UUID, HashSet<String>>  tweetLikedBy = stats.getTweetLikedBy();
		tweetLikedBy.put(tweetReplyUUID, emptyHashSet);
		stats.setTweetLikedBy(tweetLikedBy);
		
		HashMap<UUID, HashSet<String>> tweetSharedWith = stats.getTweetSharedWith();
		HashMap<String, HashSet<String>> userFollowers = stats.getUserFollowers();
		HashSet<String> temp = userFollowers.getOrDefault(user, emptyHashSet);
		//get the sender of the original message
		String senderOfOriginalTweet = tweetSentBy.get(originaltweet);
		temp.add(senderOfOriginalTweet);
		tweetSharedWith.put(tweetReplyUUID, temp);
		stats.setTweetSharedWith(tweetSharedWith);
		
		//set the parent of the tweet
		HashMap<UUID, UUID> tweetIsAReplyTo = stats.getTweetIsAReplyTo();
		tweetIsAReplyTo.put(tweetReplyUUID, originaltweet);
		stats.setTweetIsAReplyTo(tweetIsAReplyTo);
		
		System.out.println();
		System.out.println();
	}
	
	@After("afterLike()")
	public void afterLikeAdvice(JoinPoint joinpoint) {
		String user = (String)joinpoint.getArgs()[0]; 
		UUID tweetUUID = (UUID)joinpoint.getArgs()[1];
		HashSet<String> emptyHashSet = new HashSet<String>();
		
		HashMap<UUID, HashSet<String>> tweetLikedBy = stats.getTweetLikedBy();
		HashSet<String> temp = tweetLikedBy.getOrDefault(tweetUUID, emptyHashSet);
		temp.add(user);
		tweetLikedBy.put(tweetUUID, temp);
		stats.setTweetLikedBy(tweetLikedBy);
		
		HashMap<UUID, Integer> tweetLikes = stats.getTweetLikes();
		tweetLikes.put(tweetUUID, tweetLikes.get(tweetUUID)+1);
		stats.setTweetLikes(tweetLikes);
		
		System.out.println("The new messageLikedBy is " + tweetLikedBy);
		System.out.println();
	}
	
	@After("afterFollow()")
	public void afterFollowAdvice(JoinPoint joinpoint) {
		String follower = (String)joinpoint.getArgs()[0]; 
		String followee = (String)joinpoint.getArgs()[1];
		HashMap<String, HashSet<String>> userBlockedBy = stats.getUserBlockedBy();
		HashSet<String> emptyHashSet = new HashSet<String>();
		
		if(userBlockedBy.getOrDefault(follower, emptyHashSet).contains(followee)) {
			//add to the spare list of followee
			HashMap<String, HashSet<String>> userFollowersButBlocked = stats.getUserFollowersButBlocked();
			HashSet<String> temp = userFollowersButBlocked.getOrDefault(followee, emptyHashSet);
			temp.add(follower);
			userFollowersButBlocked.put(followee, temp);
			stats.setUserFollowersButBlocked(userFollowersButBlocked);
			System.out.println("The new setUserFollowersButBlocked is " + userFollowersButBlocked);
			
		} else {
			HashMap<String, HashSet<String>> userFollowers = stats.getUserFollowers();
			HashSet<String> temp = userFollowers.getOrDefault(followee, emptyHashSet);
			temp.add(follower);
			userFollowers.put(followee, temp);
			stats.setUserFollowers(userFollowers);
			System.out.println("The new userFollowers is " + userFollowers);
		}
		
		HashMap<String, Integer> maxCountOfFollowers = stats.getMaxCountOfFollowers();
		maxCountOfFollowers.put(followee, maxCountOfFollowers.getOrDefault(followee, 0)+1);
		stats.setMaxCountOfFollowers(maxCountOfFollowers);
		System.out.println("The new maxCountOfFollowers is " + maxCountOfFollowers);
		System.out.println();
	}
	
	@After("afterBlock()")
	public void afterBlockAdvice(JoinPoint joinpoint) {
		String blocker = (String)joinpoint.getArgs()[0]; 
		String blockee = (String)joinpoint.getArgs()[1];
		HashSet<String> emptyHashSet = new HashSet<String>();
		
		HashMap<String, HashSet<String>> userBlockedBy = stats.getUserBlockedBy();
		HashSet<String> temp = userBlockedBy.getOrDefault(blockee, emptyHashSet);
		temp.add(blocker);
		userBlockedBy.put(blockee, temp);
		stats.setUserBlockedBy(userBlockedBy);
		System.out.println("The new userBlockedBy is " + userBlockedBy);
		System.out.println();
		
		//remove the blocked person from the follow list
		HashMap<String, HashSet<String>> userFollowers = stats.getUserFollowers();
		temp = userFollowers.getOrDefault(blocker, emptyHashSet);
		if(temp.contains(blockee)) {
			temp.remove(blockee);
			userFollowers.put(blocker, temp);
			stats.setUserFollowers(userFollowers);
		}
		HashMap<String, HashSet<String>> userFollowersButBlocked = stats.getUserFollowersButBlocked();
		temp = userFollowersButBlocked.getOrDefault(blocker, emptyHashSet);
		temp.add(blockee);
		userFollowersButBlocked.put(blocker, temp);
		stats.setUserFollowersButBlocked(userFollowersButBlocked);
	}
	
	
	
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void afterTweet() {}
	
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void afterReply() {}
	
	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void afterLike() {}
	
	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void afterFollow() {}
	
	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void afterBlock() {}
	
}
