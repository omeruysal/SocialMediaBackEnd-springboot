package com.example.demo.model.twitVM;


import com.example.demo.model.Twit;
import com.example.demo.model.userVM.UserVM;

import lombok.Data;

@Data
public class TwitVM {
	
	private long id;
	
	private String content;
	
	private long timestamp; //milisaniye olarak almak icin long tanimli
	
	private UserVM user;
	
	public TwitVM(Twit twit) {
		
		this.setId(twit.getId());

		this.setContent(twit.getContent());

		this.setTimestamp(twit.getTimestamp().getTime());

		this.setUser(new UserVM(twit.getUser()));
	}
}
