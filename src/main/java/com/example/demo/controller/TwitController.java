package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.anotations.CurrentUser;
import com.example.demo.model.Twit;
import com.example.demo.model.User;
import com.example.demo.model.twitVM.TwitVM;
import com.example.demo.service.TwitService;

@RestController
@RequestMapping("/api/1.0")
public class TwitController {
	@Autowired
	TwitService twitService;

	@PostMapping("/twites")
	public ResponseEntity<?> createUser(@Valid @RequestBody Twit twit, @CurrentUser User user) { // user objesi bu parametreye usmeden once bir
																									// validation logic'inden gecer

		twitService.save(twit, user);
		return ResponseEntity.ok("Basarili");
	}

	
	
	@GetMapping("/twites") //Anasayfa tweetleri
	public Page<TwitVM> getTwits(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page) {

		return twitService.getTwits(page).map(TwitVM::new);
	}
	
	
	@GetMapping("/twites/{id:[0-9]+}")//Anasayfa eski tweetler ve yeni tweetler   // verilen id'den kucuk tweetler icin.Tweetler en son atilan en ustte gosterilir //id nin 0-9 arasinda bir sayi olucagini soyleriz
	public ResponseEntity<?> getTwitsRelative(@PathVariable long id,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page,
			@RequestParam(name = "count",required =false , defaultValue = "false") boolean count,
			@RequestParam(name="direction",defaultValue="before") String direction) {
		
		
		if(count) {//count true ise verilen id'li tweet'ten daha eski tweetlerin sayisi donulmeli
			long newTwitCount = twitService.getNewTwitCount(id);
			
			Map<String , Long> response = new HashMap<>();
			response.put("count", newTwitCount);
			
			return ResponseEntity.ok(response);
		}
		if(direction.equals("after")){ //direction after ise demektir ki guncel twit listesi donmemiz lazim
			List<TwitVM> newTwits = twitService.getNewTwites(id,page.getSort())
					.stream().map(TwitVM::new).collect(Collectors.toList());
			
			return ResponseEntity.ok(newTwits);
		}

		return ResponseEntity.ok(twitService.getOldTwits(id, page).map(TwitVM::new));
	}

	
	@GetMapping("/users/twites/{username}")//Userpage tweetleri
	public Page<TwitVM> getTwitsByUsername(@PathVariable String username, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable page) {
	
		return twitService.getTwitsByUser(username , page).map(TwitVM::new);
	}
	
	
	
	@GetMapping("/users/twites/{username}/{id:[0-9]+}")//Userpage eski tweetleri ve yeni tweetler 
	public ResponseEntity<?> getTwitsRelativeByUsername(@PathVariable long id ,
			@PathVariable String username,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page,
			@RequestParam(name = "count",required =false , defaultValue = "false") boolean count,
			@RequestParam(name="direction",required =false ,defaultValue="before") String direction) {
	
		
		if(count) {
			long newTwitCount = twitService.getNewTwitCountUser(id,username);
			
			Map<String , Long> response = new HashMap<>();
			response.put("count", newTwitCount);
			
			return ResponseEntity.ok(response);
		}
	if(direction.equals("after")){ //direction after ise demektir ki kisinin guncel twit listesi donmemiz lazim
			
			List<TwitVM> newTwits = twitService.getNewTwitesByUser(id,page.getSort(), username)
					.stream().map(TwitVM::new).collect(Collectors.toList());
			
			return ResponseEntity.ok(newTwits);
		}
	
		return ResponseEntity.ok(twitService.getOldTwitsByUser(id, username , page).map(TwitVM::new));
	}

}
