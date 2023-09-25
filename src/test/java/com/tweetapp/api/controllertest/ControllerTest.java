package com.tweetapp.api.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
//import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tweetapp.api.controller.TweetAppController;
import com.tweetapp.api.dto.LoginRequestDto;
import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;
import com.tweetapp.api.service.TweetService;
import com.tweetapp.api.service.TweetServiceImpl;
import com.tweetapp.api.service.UserService;
import com.tweetapp.api.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@RunWith(SpringRunner.class)
public class ControllerTest {
	private MockMvc mockMvc;

	@CreatedDate
	private LocalDateTime postDate;

	User user;
	@Mock
	private UserService userServiceMock = new UserServiceImpl(null, null);

	@Mock
	private TweetService tweetServiceMock = new TweetServiceImpl(null, null);

	@InjectMocks
	private TweetAppController userController;

	@Test
	public void registerSuccess() throws Exception {
		User mockUser = new User();
		mockUser.setEmail("fse@gmail.com");
		mockUser.setFirstName("admin");
		mockUser.setLastName("admin");
		mockUser.setPassword("admin");
		mockUser.setUsername("admin");

		when(userServiceMock.createUser(mockUser)).thenReturn(mockUser);
		ResponseEntity<Object> response = userController.registerUser(mockUser);
		assertEquals(mockUser, response.getBody());

	}

	@Test
	public void forgotPassword() throws Exception {
		String username = "Admin";
		HashMap<String, String> check = new HashMap<String, String>();
		check.put(username, username);
		when(userServiceMock.forgotPassword(username)).thenReturn(check);
		HashMap<String, String> passwordCheck = (HashMap<String, String>) userController.forgotPassword(username);
		assertEquals(check, passwordCheck);

	}

	@Test
	public void resetPassword() throws Exception {
		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setUserName("admin");
		loginRequestDto.setPassword("admin");
		HashMap<String, String> check = new HashMap<String, String>();
		check.put(loginRequestDto.getUserName(), loginRequestDto.getPassword());
		when(userServiceMock.resetPassword(loginRequestDto.getUserName(), loginRequestDto.getPassword()))
				.thenReturn(check);
		HashMap<String, String> passwordReset = (HashMap<String, String>) userController
				.resetUserPassword(loginRequestDto);
		assertEquals(check, passwordReset);

	}

	@Test
	public void postTweetbyUser() throws Exception {

		User mockUser = new User();
		mockUser.setEmail("fse@gmail.com");
		mockUser.setFirstName("admin");
		mockUser.setLastName("admin213");
		mockUser.setPassword("admin456");
		mockUser.setUsername("admin");

		Tweet userTweets = new Tweet();
		userTweets.setId("1");
		userTweets.setTweetName("Demo Tweet");
		userTweets.setPostDate(postDate);
		userTweets.setTweetTag("Demo TweetTag");
		userTweets.setLikes(98);
		userTweets.setUser(user);

		when(tweetServiceMock.postTweetByUsername(userTweets, mockUser.getUsername())).thenReturn(userTweets);
		ResponseEntity<Tweet> response = userController.postTweetByUser(mockUser.getUsername(), userTweets);
		assertEquals(userTweets, response.getBody());

	}

	@Test
	public void updateTweetByUser() throws Exception {

		User mockUpdateTweet = new User();

		mockUpdateTweet.setEmail("fse@gmail.com");
		mockUpdateTweet.setFirstName("admin");
		mockUpdateTweet.setLastName("admin213");
		mockUpdateTweet.setPassword("admin456");
		mockUpdateTweet.setUsername("admin");

		Tweet mockTweets = new Tweet();
		mockTweets.setId("1");
		mockTweets.setTweetName("Demo Tweet");
		mockTweets.setPostDate(postDate);
		mockTweets.setTweetTag("Demo TweetTag");
		mockTweets.setLikes(98);
		mockTweets.setUser(user);

		Tweet mockTweetTest = new Tweet();
		mockTweets.setId("1");
		mockTweets.setTweetName("Demo Tweet updated");
		mockTweets.setPostDate(postDate);
		mockTweets.setTweetTag("Demo TweetTag");
		mockTweets.setLikes(98);
		mockTweets.setUser(user);

		when(tweetServiceMock.editTweet(mockUpdateTweet.getUsername(), mockTweetTest, mockTweets.getId()))
				.thenReturn(mockTweetTest);
		ResponseEntity<Tweet> response = userController.updateTweetByUser(mockUpdateTweet.getUsername(),
				mockTweets.getId(), mockTweetTest);
		assertEquals(mockTweetTest, response.getBody());

	}

	@Test
	public void getAllTweets() throws Exception {

		List<Tweet> tweetResponseList = new ArrayList<>();
		Tweet userTweets = new Tweet();
		userTweets.setId("1");
		userTweets.setTweetName("Demo Tweet");
		userTweets.setPostDate(postDate);
		userTweets.setTweetTag("DemoTweetTag");
		userTweets.setLikes(98);
		userTweets.setUser(user);

		tweetResponseList.add(userTweets);
		when(tweetServiceMock.getAllTweets()).thenReturn(tweetResponseList);
		ResponseEntity<List<Tweet>> response = userController.getAllTweets();
		assertEquals(tweetResponseList, response.getBody());
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void searchUser() throws Exception {

		List<User> userList = new ArrayList<>();
		User userSearch = new User();
		userSearch.setEmail("fse@gmail.com");
		userSearch.setFirstName("admin");
		userSearch.setLastName("admin");
		userSearch.setPassword("admin");
		userSearch.setUsername("admin");
		userList.add(userSearch);

		when(userServiceMock.getUserByUsername("admin")).thenReturn(userList);
		ResponseEntity<List<User>> response = userController.searchUser(userSearch.getUsername());
		assertEquals(userList, response.getBody());
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void deletebyUser() throws Exception {
		String Status = "Deleted Successfully";

		User userStatus = new User();

		userStatus.setEmail("fse1@gmail.com");
		userStatus.setFirstName("admin");
		userStatus.setLastName("admin");
		userStatus.setPassword("admin");
		userStatus.setUsername("admin");

		Tweet userTweets = new Tweet();
		userTweets.setId("7");
		userTweets.setTweetName("Demo delete Tweet");
		userTweets.setPostDate(postDate);
		userTweets.setTweetTag("DemoTweetTag");
		userTweets.setLikes(98);
		userTweets.setUser(user);

		when(tweetServiceMock.deleteTweetById(userTweets.getId(), userStatus.getUsername())).thenReturn(Status);
		String actual = userController.deleteTweetByUser(userStatus.getUsername(), userTweets.getId());
		assertEquals(Status, actual);

	}

	@Test
	public void likeTweetbyId() throws Exception {

		User userLikes = new User();

		userLikes.setEmail("fse1@gmail.com");
		userLikes.setFirstName("admin");
		userLikes.setLastName("admin");
		userLikes.setPassword("admin");
		userLikes.setUsername("admin");

		Tweet userTweets = new Tweet();
		userTweets.setId("7");
		userTweets.setTweetName("Demo delete Tweet");
		userTweets.setPostDate(postDate);
		userTweets.setTweetTag("DemoTweetTag");
		userTweets.setLikes(99);
		userTweets.setUser(user);

		tweetServiceMock.likeTweetById(userTweets.getId());
		ResponseEntity<HttpStatus> Status = userController.likeTweetByUser(userLikes.getUsername(), userTweets.getId());
		assertEquals(200, Status.getStatusCodeValue());

	}

	@Test
	public void getAllUsersPositiveFlow() throws Exception {
		List<User> usersList = new ArrayList<>();
		User allUser = new User();
		allUser.setEmail("fse@gmail.com");
		allUser.setFirstName("admin");
		allUser.setLastName("admin");
		allUser.setPassword("admin");
		allUser.setUsername("admin");
		usersList.add(allUser);


		when(userServiceMock.getAllUsers()).thenReturn(usersList);
		ResponseEntity<List<User>> allUserList = userController.getAllUsers();

		assertEquals(usersList, allUserList.getBody());
	}

	@Test
	public void getAllTweetsbyUserPositiveFlow() throws Exception {

		User register = new User();

		register.setEmail("fse@gmail.com");
		register.setFirstName("admin");
		register.setLastName("admin");
		register.setPassword("admin");
		register.setUsername("admin");

		List<Tweet> tweetResponse1List = new ArrayList<>();
		Tweet userTweets = new Tweet();
		userTweets.setId("1");
		userTweets.setTweetName("Demo Tweet");
		userTweets.setPostDate(postDate);
		userTweets.setTweetTag("DemoTweetTag");
		userTweets.setLikes(98);
		userTweets.setUser(register);

		tweetResponse1List.add(userTweets);

		when(tweetServiceMock.getAllTweetsByUsername(register.getUsername())).thenReturn(tweetResponse1List);
		ResponseEntity<List<Tweet>> Users = userController.getAllTweetsByUser(register.getUsername());
		assertEquals(tweetResponse1List, Users.getBody());
	}

}
