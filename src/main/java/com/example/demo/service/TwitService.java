package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Twit;
import com.example.demo.model.User;
import com.example.demo.repository.TwitRepository;

@Service
public class TwitService {
	
	@Autowired
	TwitRepository twitRepository;
	
	@Autowired
	UserService userService;
	
	public void save(Twit twit, User user) {
		 twit.setTimestamp(new Date());
		 twit.setUser(user);
		 twitRepository.save(twit);
	}

	public Page<Twit> getTwits(Pageable page) {
		
		return twitRepository.findAll(page);
	}

	public Page<Twit> getTwitsByUser(String username, Pageable page) {
		User inDB = userService.getByUsername(username);
		return twitRepository.findByUser(inDB, page);
	}

	public Page<Twit> getOldTwits(long id, Pageable page) {
		
		return twitRepository.findByIdLessThan(id, page);
	}

	public Page<Twit> getOldTwitsByUser(long id, String username, Pageable page) {
		User inDB = userService.getByUsername(username);
	
		return twitRepository.findByIdLessThanAndUser(id, inDB, page);
	}

	public long getNewTwitCount(long id) {
		
		return twitRepository.countByIdGreaterThan(id);
	}

	public long getNewTwitCountUser(long id, String username) {
		User inDB = userService.getByUsername(username);
		
		return twitRepository.countByIdGreaterThanAndUser(id, inDB);
	}

	public List<Twit> getNewTwites(long id, Sort sort) {
		
		return twitRepository.findByIdGreaterThan(id, sort);
	}

	public List<Twit> getNewTwitesByUser(long id, Sort sort,String username) {
		User inDB = userService.getByUsername(username);
		
		return twitRepository.findByIdGreaterThanAndUser(id, sort, inDB);
	}

}
