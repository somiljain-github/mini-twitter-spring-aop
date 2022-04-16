package edu.sjsu.cmpe275.aop.tweet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class TweetStatsServiceImpl implements TweetStatsService {
	//Details related to the messages
		//Message ID: Message Description
		private HashMap<UUID, String> tweetMessage = new HashMap<UUID, String>();
		//Message ID: User id of the sender
		private HashMap<UUID, String> tweetSentBy = new HashMap<UUID, String>();
		//Message id: depth of the message(for length of thread)
		private HashMap<UUID, Integer> tweetThreadDepth = new HashMap<UUID, Integer>();
		//Message id: number of valid likes
		private HashMap<UUID, Integer> tweetLikes = new HashMap<UUID, Integer>();
		//Message id: number of valid likes
		private HashMap<UUID, HashSet<String>> tweetLikedBy = new HashMap<UUID, HashSet<String>>();
		//Message id: a list of the users the message was shared with
		private HashMap<UUID, HashSet<String>> tweetSharedWith = new HashMap<UUID, HashSet<String>>();
		//MessageID: is a reply to which message
		private HashMap<UUID, UUID> tweetIsAReplyTo = new HashMap<UUID, UUID>();
		
		//Details related to the users
		//User ID: a list of users who have blocked the current user
		private HashMap<String, HashSet<String>> userBlockedBy = new HashMap<String, HashSet<String>>();
		//User ID: MaxNumberOfFollowers at any point since the last reset
		private HashMap<String, Integer> maxCountOfFollowers = new HashMap<String, Integer>();
		//User ID: People who follow the user
		private HashMap<String, HashSet<String>> userFollowers = new HashMap<String, HashSet<String>>();
		//User ID: People who follow the user but is blocked by the user.
		//This list will help in creating the share list for the message in O(1) time and will help extend the application if an unblock feature is to be implented in future
		private HashMap<String, HashSet<String>> userFollowersButBlocked = new HashMap<String, HashSet<String>>();	
		
		//============================Getters and Setters for the HashMaps==========================================

		public HashMap<UUID, String> getTweetMessage() {
			return tweetMessage;
		}

		public void setTweetMessage(HashMap<UUID, String> tweetMessage) {
			this.tweetMessage = tweetMessage;
		}

		public HashMap<UUID, String> getTweetSentBy() {
			return tweetSentBy;
		}

		public void setTweetSentBy(HashMap<UUID, String> tweetSentBy) {
			this.tweetSentBy = tweetSentBy;
		}

		public HashMap<UUID, Integer> getTweetThreadDepth() {
			return tweetThreadDepth;
		}

		public void setTweetThreadDepth(HashMap<UUID, Integer> tweetThreadDepth) {
			this.tweetThreadDepth = tweetThreadDepth;
		}

		public HashMap<UUID, Integer> getTweetLikes() {
			return tweetLikes;
		}

		public void setTweetLikes(HashMap<UUID, Integer> tweetLikes) {
			this.tweetLikes = tweetLikes;
		}

		public HashMap<UUID, HashSet<String>> getTweetLikedBy() {
			return tweetLikedBy;
		}

		public void setTweetLikedBy(HashMap<UUID, HashSet<String>> tweetLikedBy) {
			this.tweetLikedBy = tweetLikedBy;
		}

		public HashMap<UUID, HashSet<String>> getTweetSharedWith() {
			return tweetSharedWith;
		}

		public void setTweetSharedWith(HashMap<UUID, HashSet<String>> tweetSharedWith) {
			this.tweetSharedWith = tweetSharedWith;
		}

		public HashMap<UUID, UUID> getTweetIsAReplyTo() {
			return tweetIsAReplyTo;
		}

		public void setTweetIsAReplyTo(HashMap<UUID, UUID> tweetIsAReplyTo) {
			this.tweetIsAReplyTo = tweetIsAReplyTo;
		}

		public HashMap<String, HashSet<String>> getUserBlockedBy() {
			return userBlockedBy;
		}

		public void setUserBlockedBy(HashMap<String, HashSet<String>> userBlockedBy) {
			this.userBlockedBy = userBlockedBy;
		}

		public HashMap<String, Integer> getMaxCountOfFollowers() {
			return maxCountOfFollowers;
		}

		public void setMaxCountOfFollowers(HashMap<String, Integer> maxCountOfFollowers) {
			this.maxCountOfFollowers = maxCountOfFollowers;
		}

		public HashMap<String, HashSet<String>> getUserFollowers() {
			return userFollowers;
		}

		public void setUserFollowers(HashMap<String, HashSet<String>> userFollowers) {
			this.userFollowers = userFollowers;
		}

		public HashMap<String, HashSet<String>> getUserFollowersButBlocked() {
			return userFollowersButBlocked;
		}

		public void setUserFollowersButBlocked(HashMap<String, HashSet<String>> userFollowersButBlocked) {
			this.userFollowersButBlocked = userFollowersButBlocked;
		}

	@Override
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getMostPopularMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getMostProductiveReplier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getMostLikedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMostUnpopularFollower() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getLongestMessageThread() {
		// TODO Auto-generated method stub
		return null;
	}

}



