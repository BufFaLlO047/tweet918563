package com.tweetapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {

	private User user;
	private String loginStatus;
	private String token;
	private String ErrorMessage;
}
