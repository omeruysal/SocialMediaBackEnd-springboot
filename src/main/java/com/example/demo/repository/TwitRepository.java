package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Twit;
import com.example.demo.model.User;

@Repository
public interface TwitRepository extends JpaRepository<Twit, Integer>{
	
	public Page<Twit> findByUser(User user, Pageable page);
	
	public Page<Twit> findByIdLessThan(long id, Pageable page);
	
	public Page<Twit> findByIdLessThanAndUser(long id, User user, Pageable page);
	
	public long countByIdGreaterThan(long id); //verdigimiz id den buyuk olan tweetlerin sayisini doner
	
	public long countByIdGreaterThanAndUser(long id, User user);//verdigimiz id den buyuk olan verdigimiz kisinin tweet sayisi
	
	public List<Twit> findByIdGreaterThan(long id, Sort sort);//yeni tweetler doner
	
	public List<Twit> findByIdGreaterThanAndUser(long id, Sort sort , User user);//kisinin yeni tweetleri doner

}
