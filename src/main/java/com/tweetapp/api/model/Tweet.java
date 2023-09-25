package com.tweetapp.api.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.api.dto.ReplyDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "tweets")
public class Tweet {

	@Id
	private String id;
	private String tweetName;
	@CreatedDate
	private LocalDateTime postDate;
	private long likes;
	private User user;
	private List<ReplyDto> replies;
	private String tweetTag;

}
