package com.tweetapp.api.service;

import java.util.List;

import com.tweetapp.api.dto.ReplyDto;
import com.tweetapp.api.exception.IncorrectOrDeletedTweet;
import com.tweetapp.api.model.Tweet;

public interface TweetService {
	
	Tweet postTweet(Tweet tweet);
	Tweet postTweetByUsername(Tweet tweet, String username);
	void deleteTweet(Tweet tweet);
	List<Tweet> getAllTweets();
	List<Tweet> getAllTweetsByUsername(String username);
	void replyTweetById(ReplyDto replyTweet, String parentTweetId) throws IncorrectOrDeletedTweet;
	String deleteTweetById(String tweetId,String username) ;
	void likeTweetById(String tweetId);
	Tweet editTweet(String username, Tweet tweet, String tweetId);


}
