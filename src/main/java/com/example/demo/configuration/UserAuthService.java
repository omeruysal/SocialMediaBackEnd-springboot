package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserAuthService implements UserDetailsService { //bu class sayesinde forma gelen bilgilerden userin nerde oldugunu soyleriz. Eger bu class olmasaydi Spring security user bilgilerini kullanamazdi

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User inDB = userRepository.findByUsername(username);
		
		if(inDB==null)
			throw new UsernameNotFoundException("User not found"); //Spring security ile gelen bu hatayi firlatiriz 

		return (UserDetails)inDB; //fonksiyon donus tipi olarak UserDetails ister, bu yuzden UserDetails implement eden bir class yaratiriz
	}

}
