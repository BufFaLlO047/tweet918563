package com.tweetapp.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.api.dto.LoginRequestDto;
import com.tweetapp.api.dto.ReplyDto;
import com.tweetapp.api.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.api.exception.UsernameAlreadyExists;
import com.tweetapp.api.kafka.ProducerService;
import com.tweetapp.api.model.ErrorMessages;
import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;
import com.tweetapp.api.model.UserResponse;
import com.tweetapp.api.service.TweetService;
import com.tweetapp.api.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {

	@Autowired
	UserService userService;

	@Autowired
	TweetService tweetService;

	@Autowired
	ProducerService producerService;

	Logger logger = LoggerFactory.getLogger(TweetAppController.class);

	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@RequestBody User user) throws UsernameAlreadyExists {
		try {
			logger.info("User created successfully!!!...");
			return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
		} catch (UsernameAlreadyExists e) {
			throw new UsernameAlreadyExists(ErrorMessages.USER_ALREADY_EXISTS.getMessage());
		}

	}

	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(Model model, @RequestBody LoginRequestDto loginRequestDto,
			HttpServletRequest request) throws InvalidUsernameOrPasswordException {

		logger.debug("----Inside TweetAppController-> loginUser()------");
		try {
			UserResponse authUser = userService.loginUser(loginRequestDto.getUserName(), loginRequestDto.getPassword());
			System.out.print(authUser);
			if (authUser != null) {
				request.getSession().setAttribute("user", loginRequestDto.getUserName());
				return new ResponseEntity<UserResponse>(authUser, HttpStatus.OK);
			} else {
				throw new InvalidUsernameOrPasswordException(ErrorMessages.INVALID_CREDENTIALS.getMessage());
			}
		} catch (InvalidUsernameOrPasswordException e) {
			throw new InvalidUsernameOrPasswordException(ErrorMessages.INVALID_CREDENTIALS.getMessage());
		}
	}

	@GetMapping("/{username}/forgot")
	public Map<String, String> forgotPassword(@PathVariable("username") String username) {
		logger.info("Forgot Password request received with username: " + username);
		return new HashMap<String, String>(userService.forgotPassword(username));

	}

	@PostMapping("/reset")
	public Map<String, String> resetUserPassword(@RequestBody LoginRequestDto loginRequestDto) {
		logger.info("Request to reset the password");
		return new HashMap<String, String>(
				userService.resetPassword(loginRequestDto.getUserName(), loginRequestDto.getPassword()));

	}

	@GetMapping("/all")
	public ResponseEntity<List<Tweet>> getAllTweets() {
		logger.info("Retreive all the tweets by the all users");
		return new ResponseEntity<>(tweetService.getAllTweets(), HttpStatus.OK);
	}

	@GetMapping("/users/all")
	public ResponseEntity<List<User>> getAllUsers() {
		logger.info("Retrived all the users");
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@GetMapping("/user/search/{username}")
	public ResponseEntity<List<User>> searchUser(@PathVariable("username") String username)
			throws InvalidUsernameOrPasswordException {
		logger.info("Retriving the user by the username");
		return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
	}

	@GetMapping("/{username}")
	public ResponseEntity<List<Tweet>> getAllTweetsByUser(@PathVariable("username") String username) {
		logger.info("Retriving all the tweets by the user");
		return new ResponseEntity<>(tweetService.getAllTweetsByUsername(username), HttpStatus.OK);
	}

	@PostMapping("/{username}/add")
	public ResponseEntity<Tweet> postTweetByUser(@PathVariable("username") String username, @RequestBody Tweet tweet) {
		logger.info("Tweet successfully posted by the user");
		return new ResponseEntity<>(tweetService.postTweetByUsername(tweet, username), HttpStatus.OK);
	}

	@PutMapping("/{username}/update/{id}")
	public ResponseEntity<Tweet> updateTweetByUser(@PathVariable("username") String username,
			@PathVariable("id") String tweetId, @RequestBody Tweet tweet) {
		logger.info("Tweet successfully updated by the user");
		return new ResponseEntity<>(tweetService.editTweet(username, tweet, tweetId), HttpStatus.OK);
	}

	@DeleteMapping("/{username}/delete/{id}")
	public String deleteTweetByUser(@PathVariable("username") String username, @PathVariable("id") String tweetId) {

		String Status = tweetService.deleteTweetById(tweetId, username);
		logger.info("Tweet deleted by the user");
		return Status;
	}

	@PutMapping("/{username}/like/{id}")
	public ResponseEntity<HttpStatus> likeTweetByUser(@PathVariable("username") String username,
			@PathVariable("id") String tweetId) {
		tweetService.likeTweetById(tweetId);
		logger.info("Like a tweet ");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{username}/reply/{id}")
	public ResponseEntity<String> replyTweetByUser(@PathVariable("username") String username,
			@PathVariable("id") String tweetId, @RequestBody ReplyDto replyTweet) {

		try {
			logger.info("Replying to the tweet by user");
			tweetService.replyTweetById(replyTweet, tweetId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
