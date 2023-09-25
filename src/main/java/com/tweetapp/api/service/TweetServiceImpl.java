package com.tweetapp.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.api.dto.ReplyDto;
import com.tweetapp.api.exception.IncorrectOrDeletedTweet;
import com.tweetapp.api.kafka.ProducerService;
import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;
import com.tweetapp.api.repository.TweetRepository;
import com.tweetapp.api.repository.UserRepository;

@Service
public class TweetServiceImpl implements TweetService {

	@Autowired
	UserRepository userRepository;

	
	@Autowired 
	ProducerService producerService;
	public static List<ReplyDto> replies = new ArrayList<>();
	
	Logger logger = LoggerFactory.getLogger(TweetServiceImpl.class);

	private TweetRepository tweetRepository;
	private TokenService tokenService;

	public TweetServiceImpl(TweetRepository tweetRepo, TokenService tokenService) {
		this.tweetRepository = tweetRepo;
		this.tokenService = tokenService;
	}

	@Override
	public Tweet postTweet(Tweet tweet) {

		 producerService.sendMessage("Tweet posted by user"); 
		logger.info("Tweet posted successfully....");
		return tweetRepository.save(tweet);
	}

	@Override
	public Tweet editTweet(String username, Tweet tweet, String tweetId) {

		 producerService.sendMessage("Tweet is updated.."); 
		LocalDateTime local = LocalDateTime.now();

		User user = userRepository.findByUsername(username);
		Optional<Tweet> tweetop = tweetRepository.findById(tweetId);
		if (tweetop.isPresent()) {
			Tweet foundTweet = tweetop.get();
			foundTweet.setTweetName(tweet.getTweetName());
			foundTweet.setUser(user);
			foundTweet.setTweetTag("");
			foundTweet.setPostDate(local);
			return tweetRepository.save(foundTweet);
		}
		logger.info("Tweet is updated successfully...");

		return tweetRepository.save(tweet);
	}

	

	@Override
	public void deleteTweet(Tweet tweet) {
		tweetRepository.delete(tweet);
		logger.info("Tweet deleted successfully...");
	}

	@Override
	public List<Tweet> getAllTweets() {
		producerService.sendMessage("Received request to send all tweet data."); 
		logger.info("Retriving all the tweet data");
		return tweetRepository.findAll();
	}

	@Override
	public List<Tweet> getAllTweetsByUsername(String username) {
		logger.info("Retriving tweets of user: " + username);
		return tweetRepository.findByUserUsername(username);
	}

	@Override
	public Tweet postTweetByUsername(Tweet tweet, String username) {
		User user = userRepository.findByUsername(username);
		tweet.setUser(user);
		 producerService.sendMessage("Tweet posted by the user : "+username);
		logger.info("Tweet posted by user: " + username);
		return tweetRepository.save(tweet);

	}

	@Override
	public String deleteTweetById(String tweetId, String userName) {
		String name = null;
		Optional<Tweet> tweetOp = tweetRepository.findById(tweetId);
		if (tweetOp.isPresent()) {
			Tweet presentTweet = tweetOp.get();
			name = presentTweet.getUser().getUsername();
		}
		if (name.equals(userName)) {
			tweetRepository.deleteById(tweetId);
			return "Deleted Successfully";
		} else {
			return "This request is not authorized please login again";
		}

	}

	@Override
	public void replyTweetById(ReplyDto replyTweet, String parentTweetId) throws IncorrectOrDeletedTweet {
		Optional<Tweet> parentTweet = tweetRepository.findById(parentTweetId);
		
		if (parentTweet.isPresent()) {
			Tweet foundTweet = parentTweet.get();
			replies.add(replyTweet);
			foundTweet.setReplies(replies);
			tweetRepository.save(foundTweet);
		} else {
			throw new IncorrectOrDeletedTweet("Incorrect or deleted parent tweet id.");
		}

	}

	@Override
	public void likeTweetById(String tweetId) {
		Optional<Tweet> tweet = tweetRepository.findById(tweetId);
		logger.info("Liked Tweet with Id: {} is {}", tweetId, tweet.get());
		if (tweet.isPresent()) {
			tweet.get().setLikes(tweet.get().getLikes() + 1);
			tweetRepository.save(tweet.get());
		}

	}

}
