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
		//============================Over riding the functions from TweetStatsService Interface==========================================
		@Override
		public void resetStatsAndSystem() {
			tweetMessage.clear();
			tweetSentBy.clear();
			tweetThreadDepth.clear();
			tweetLikes.clear();
			tweetLikedBy.clear();
			tweetSharedWith.clear();
			tweetIsAReplyTo.clear();
			
			userBlockedBy.clear();
			maxCountOfFollowers.clear();
			userFollowers.clear();
			userFollowersButBlocked.clear();
		}
	    
		@Override
		public int getLengthOfLongestTweet() {
			/**
			 * @returns the length of longest message a user successfully sent since the beginning or last reset. 
			 * Replied messages count as well, but each replying message is an independent message on its own.
			 * If no messages were successfully tweeted, return 0.
			 */
			
			Integer maxLength = 0;
			if(tweetMessage.isEmpty())
			{
				//No tweets. Returning zero;
				return maxLength;
			}
			
			for (String messageContent: tweetMessage.values()){
				maxLength = Math.max(maxLength, messageContent.length());
			}
			
			return maxLength;
			
		}

		
		@Override
		public String getMostFollowedUser() {
			/**
			 * @returns the user who is being followed by the biggest number of different users since the beginning or last reset. 
			 * If there is a tie, return the 1st of such users based on alphabetical order. 
			 * If any follower has been blocked by the followee, this follower Still count; i.e., 
			 * Blocking or not does not affect this metric. 
			 * If someone follows him/herself, it does not count. 
			 * If no users are followed by anybody, return null.
			 */
			Integer maxFollowers = 0;
			String user = null;
			
			for(Map.Entry<String, Integer> entry: maxCountOfFollowers.entrySet()) {
				String currentUser = entry.getKey();
				Integer followers = entry.getValue();
				
				if(maxFollowers < followers) {
					maxFollowers = followers;
					user = currentUser;
				} else if(maxFollowers == followers && maxFollowers!=0) {
					if(user.compareTo(currentUser)>0) {
						user=currentUser;
						maxFollowers = followers;
					}
				}
			}
			
			return user;
		}

		@Override
		public UUID getMostPopularMessage() {
			/**
			 * @returns the message that has been shared with the biggest number of unique recipients when it is successfully tweeted. 
			 * If two messages have the same string content but different UUIDs, they are considered different for the purpose here.
			 * If there is a tie, return the message whose UUID is smaller. If no shared messages, return null. 
			 * The very original sender of a message will NOT be counted toward the number of shared users for this purpose, unless somebody else 
			 * has successfully shared the same message (based on string equality) with him.  
			 */
			
			Integer maxNumberOfUsersShared = 0;
			UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
			UUID messageUUID = emptyUUID;

			for(Map.Entry<UUID, HashSet<String>> entry : tweetSharedWith.entrySet()) {
				UUID currentMessageUUID = entry.getKey();
				HashSet<String> sharedWith = entry.getValue();
				Integer numberOfUsersShared = sharedWith.size();
				
				if(maxNumberOfUsersShared < numberOfUsersShared) {
					maxNumberOfUsersShared = numberOfUsersShared;
					messageUUID = currentMessageUUID;
				} else if(maxNumberOfUsersShared == numberOfUsersShared) {
					if(messageUUID.compareTo(currentMessageUUID) > 0 ) {
						messageUUID = currentMessageUUID;	
					}
				}
			}
			return (messageUUID.equals(emptyUUID)) ? null : messageUUID;
		}
		
		@Override
		public String getMostProductiveReplier() {
			/**
			 * @returns the most productive user.
			 * The most productive replier is determined by the total length measured in character count of all the messages successfully tweeted as a reply to another message since the beginning or last reset. 
			 * If there is a tie, return the 1st of such users based on alphabetical order. 
			 * If no users successfully tweeted, return null.
			 */
			HashMap<String, Integer> tweetLength = new HashMap<String, Integer>();
			UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
			for(Map.Entry<UUID, String> entry: tweetSentBy.entrySet()) {
				UUID currentMessage = entry.getKey();
				String currentUser = entry.getValue();
				//check if the message is a reply or not
				UUID parentTweet = tweetIsAReplyTo.getOrDefault(currentMessage, emptyUUID); 	
				if(!parentTweet.equals(emptyUUID)) {
					String message = tweetMessage.get(currentMessage);
					Integer currentLength = message.length();
					tweetLength.put(currentUser, tweetLength.getOrDefault(currentUser, 0)+currentLength);
				}
				
			}
			
			if(tweetLength.isEmpty()) {
				return null;
			}
			String user=null;
			Integer maxValue = 0;
			
			for(Map.Entry<String, Integer> entry: tweetLength.entrySet()) {
				String currentUser = entry.getKey();
				Integer currentLength = entry.getValue();
				if(maxValue < currentLength) {
					maxValue = currentLength;
					user=currentUser;
				} else if(maxValue == currentLength) 
					if(user.compareTo(currentUser) > 0) {
						user = currentUser;
				}
			}
			return user;
		}

		@Override
		public UUID getMostLikedMessage() {
			/**
			 * @returns the ID of the message that has been successfully liked by the biggest number of unique recipients when it is successfully tweeted. 
			 * If two messages are equal based on string equality but have different message IDs, they are considered as different message for this purpose.
			 * If there is a tie in the number of different recipients, return the smallest message ID. If no shared messages, return null. 
			 * */

			UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
			UUID mostLikedMessage = emptyUUID;
			Integer mostLikes = 0;
			
			for(Map.Entry<UUID, HashSet<String>> entry: tweetLikedBy.entrySet()) {
				UUID currentMessage = entry.getKey();
				HashSet<String> likes = entry.getValue();
				Integer numOfLikes = likes.size();
				if(mostLikes < numOfLikes) {
					mostLikes = numOfLikes;
					mostLikedMessage = currentMessage;
				} else if(mostLikes == numOfLikes) {
					if(mostLikedMessage.compareTo(currentMessage) > 0) {
						mostLikedMessage = currentMessage;
					}
				}
			}
			
			return (mostLikedMessage.equals(emptyUUID)) ? null : mostLikedMessage;
		}

		@Override
		public String getMostUnpopularFollower() {
			/**
			 * @return the user who is currently successfully blocked by the biggest number of different users since the beginning or last reset. 
			 * If there is a tie, return the 1st of such users based on alphabetical order. 
			 * If no follower has been successfully blocked by anyone, return null.
			 */
			String user = null;
			Integer maxBlockedCount = 0;
			
			for(Map.Entry<String, HashSet<String>> entry : userBlockedBy.entrySet()) {
				String currentUser = entry.getKey();
				HashSet<String> blockedByUsers = entry.getValue();
				Integer count = blockedByUsers.size();
				if(maxBlockedCount < count) {
					maxBlockedCount = count;
					user = currentUser;
				} else if(maxBlockedCount == count) {
					if(user.compareTo(currentUser) > 0) {
						user = currentUser;
					}
				}
			}
			return user;
		}

		@Override
		public UUID getLongestMessageThread() {
			UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
			UUID message = emptyUUID;
			Integer threadLength = 0;
			for(Map.Entry<UUID, Integer> entry : tweetThreadDepth.entrySet()) {
				UUID currentMessage = entry.getKey();
				Integer currentMessageLength = entry.getValue();
				
				if(threadLength < currentMessageLength) {
					threadLength = currentMessageLength;
					message = currentMessage;
				} else if(threadLength == currentMessageLength) {
					if(message.compareTo(currentMessage) > 0) {
						message = currentMessage;
					}
				}
			}

			return (message.equals(emptyUUID)) ? null : message;
		}
}



