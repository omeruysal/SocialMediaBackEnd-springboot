package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.Twit;
import com.example.demo.model.User;
import com.example.demo.service.TwitService;
import com.example.demo.service.UserService;

@SpringBootApplication
public class SocialMediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApplication.class, args);
	}
	
	@Bean //uygulama ayaga kalkarken burdaki run methodunu kosar
	CommandLineRunner createInitialUsers(UserService userService,TwitService twitService) {
		return (args) ->{
			for(int i=1;i<=25;i++) {
				User user = new User();
				user.setUsername("user"+i);
				user.setDisplayName("display"+i);
				user.setPassword("Password1");
				userService.save(user);
						for(int j=1;j<=20;j++) {
							Twit twit = new Twit();
							twit.setContent("Tweet "+j);
							twitService.save(twit , user);
							
						}
			}
			
		};
		
	}

}
