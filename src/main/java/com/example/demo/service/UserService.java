package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.error.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.model.userVM.UserUpdateVM;
import com.example.demo.model.userVM.UserVM;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder;
	
	@Autowired
	FileService fileService;
	
	
	public UserService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}




	public void save(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}




	public Page<User> getUsers(Pageable page, User user) {//Pageable kullarak sayfa kontrolu sinir kontrolu gibi gibi denetlemeleri springe birakabiliriz 
		
		if(user!=null) {
			return userRepository.findByUsernameNot(user.getUsername(), page);
		}
		return userRepository.findAll(page);
	}




	public User getByUsername(String username) {
		User inDb = userRepository.findByUsername(username);
		if(inDb==null) {
			throw new NotFoundException(); //user olmadigi durumda 404 doneriz
		}
		return inDb;
	}
	
	public User updatePatchExample(String username, String displayName) {
		User user = getByUsername(username);
		user.setDisplayName(displayName);
		return userRepository.save(user);
	}



	public User updateUser(String username, UserUpdateVM updatedUser) throws IOException {
		
		User inDB =  (User) getByUsername(username); //tekrardan null olup olmadigina bakmadik cunku getByUser icinde zaten bakiyoruz
		inDB.setDisplayName(updatedUser.getDisplayName());
		
		if(updatedUser.getImage()!=null) {
			String oldImageName = inDB.getImage(); //kullanici yeni fotograf ekledigi oldImage'i sileriz
			try {

				String storedFileName = fileService.writeBas64EncodedStringToFile(updatedUser.getImage()); // bu method ile frontend'ten gelen base64 stringi klasore kaydedip ismini doneriz ve o ismi dbye ekleriz
				inDB.setImage(storedFileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileService.deleteFile(oldImageName);
		}
		
		return userRepository.save(inDB); //hibernate id den farkedip zaten bir user oldugu icin otomatik olarak field guncellemesi yapar
		 
	}
	
	

}
