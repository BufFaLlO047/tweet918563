package com.tweetapp.api.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;
import com.tweetapp.api.model.UserResponse;
import com.tweetapp.api.repository.TweetRepository;
import com.tweetapp.api.repository.UserRepository;
import com.tweetapp.api.service.TokenService;
import com.tweetapp.api.service.TweetServiceImpl;
import com.tweetapp.api.service.UserServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
	private MockMvc mockMvc;

	@Mock
	private UserRepository userrepo;

	@Mock
	private TweetRepository tweetRepo;

	@Mock
	private TokenService tokenService;

	@InjectMocks
	private UserServiceImpl userServiceMock = new UserServiceImpl(userrepo, tokenService);

	@InjectMocks
	private TweetServiceImpl tweetServiceMock = new TweetServiceImpl(tweetRepo, tokenService);

	@BeforeEach
	public void setup() {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void registerPositiveTest() throws Exception {
		User registerDTO = new User();

		registerDTO.setEmail("fse@gmail.com");
		registerDTO.setFirstName("admin");
		registerDTO.setLastName("admin");
		registerDTO.setPassword("admin");
		registerDTO.setUsername("admin");

		when(userrepo.save(registerDTO)).thenReturn(registerDTO);
		User actualresponse = userServiceMock.createUser(registerDTO);

		assertEquals(registerDTO.getFirstName(), actualresponse.getFirstName());
	}

	@Test
	public void signUpPostiveTest() throws Exception {
		User user = new User();
		user.setEmail("fse@gmail.com");
		user.setFirstName("admin");
		user.setLastName("admin");
		user.setPassword("admin");
		user.setUsername("admin");
		UserResponse loginRequestDTO = new UserResponse();
		loginRequestDTO.setUser(user);
		loginRequestDTO.setLoginStatus("success");

		when(userrepo.findByUsername("admin")).thenReturn(user);

		UserResponse actualResponse = userServiceMock.loginUser(user.getUsername(), user.getPassword());
		assertEquals("success", actualResponse.getLoginStatus());
	}

	@Test
	public void getAllUsersPositiveTest() throws Exception {

		List<User> registerList = new ArrayList<>();
		User register = new User();
		// register.setId("id");
		register.setEmail("fse@gmail.com");
		register.setFirstName("admin");
		register.setLastName("admin");
		register.setPassword("admin");
		register.setUsername("admin");

		registerList.add(register);


		when(userrepo.findAll()).thenReturn(registerList);

		List<User> actualResponse = userServiceMock.getAllUsers();
		assertEquals(registerList, actualResponse);
	}

	@Test
	public void getAllTweetsPositiveTest() throws Exception {

		List<Tweet> TweetList = new ArrayList<>();
		Tweet tweet = new Tweet();
		tweet.setId("1");
		tweet.setLikes(1);
		tweet.setTweetName("Hi");
		tweet.setTweetTag("tag");

		TweetList.add(tweet);

		when(tweetRepo.findAll()).thenReturn(TweetList);

		List<Tweet> actualResponse = tweetServiceMock.getAllTweets();
		assertEquals(TweetList.size(), actualResponse.size());
	}

	@Test
	public void getAllTweetsByUsernameTest() throws Exception {

		List<User> registerList = new ArrayList<>();
		User register = new User();

		register.setEmail("fse@gmail.com");
		register.setFirstName("admin");
		register.setLastName("admin");
		register.setPassword("admin");
		register.setUsername("admin");

		registerList.add(register);

		List<Tweet> TweetList = new ArrayList<>();
		Tweet tweet = new Tweet();
		tweet.setId("1");
		tweet.setLikes(1);
		tweet.setTweetName("Hi");
		tweet.setTweetTag("tag");
		tweet.setUser(register);
		TweetList.add(tweet);

		when(tweetRepo.findByUserUsername("admin")).thenReturn(TweetList);
		List<Tweet> actualResponse = tweetServiceMock.getAllTweetsByUsername(register.getUsername());
		assertEquals(TweetList, actualResponse);

	}

	@Test
	public void postTweetTest() throws Exception {

		Tweet tweet = new Tweet();
		tweet.setId("1");
		tweet.setLikes(100);
		tweet.setTweetName("Hi");
		tweet.setTweetTag("tag");

		when(tweetRepo.save(tweet)).thenReturn(tweet);
		Tweet actualResponse = tweetServiceMock.postTweet(tweet);

		assertEquals(tweet, actualResponse);

	}

	@Test
	public void postTweetByUsernameTest() throws Exception {

		Tweet tweet = new Tweet();
		tweet.setId("1");
		tweet.setLikes(99);
		tweet.setTweetName("Hi");
		tweet.setTweetTag("tag");

		User register = new User();

		register.setEmail("fse@gmail.com");
		register.setFirstName("admin");
		register.setLastName("admin");
		register.setPassword("admin");
		register.setUsername("admin");

		when(userrepo.findByUsername("admin")).thenReturn(register);
		when(tweetRepo.save(tweet)).thenReturn(tweet);

		Tweet actualResponse = tweetServiceMock.postTweetByUsername(tweet, register.getUsername());

		assertEquals(tweet, actualResponse);

	}

}
