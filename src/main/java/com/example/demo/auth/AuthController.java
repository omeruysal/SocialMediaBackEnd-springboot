package com.example.demo.auth;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.anotations.CurrentUser;
import com.example.demo.error.ApiError;
import com.example.demo.error.Views;
import com.example.demo.model.User;
import com.example.demo.model.userVM.UserVM;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class AuthController {

	@Autowired
	UserRepository userRepository;
	

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/api/1.0/auth")
	UserVM handleAuthenticationWithSpringSecurity(@CurrentUser User user){//CurrentUser anotasyonu sayesinde parametre olarak user gecebiliriz. Yoksa authentication objesi yaratip getPrincipal yaparak cast etmemiz gerekirdi.Bu getPrincipal ise bize userAuthService'ten gelmektedir
		
		return new UserVM(user); 
	}

	
	
	
	
	
	
	
	
	
	
	
	
	//bu fonskyion eger spring security kullanmasaydik nasil authentication yapacagimizi gostermek icindi.Spring security yukaridaki fonskyionda goruldugu gibi bizim icin bu islemleri configurasyon dosyasi icinde yapmaktadir
	//PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	//@PostMapping("/api/1.0/auth")
	ResponseEntity<?> handleAuthenticationWithoutSpringSecurity(
			@RequestHeader(name = "Authorization", required = false) String authorization) {	// request headerdan
																								// authorization kismini
																								// aliriz.Gelem header
																								// icinde authorization
																								// kismi yoksa 400
																								// hatasi firlatir
		if (authorization == null) { // eger request header authorization credentials barindirmiyorsa
			ApiError error = new ApiError(401, "Unauthorized request", "/api/1.0//auth"); // normalde required kismini true yapip bu if i eklemeden de yapabilirdik
																							// fakat ekleyerek kendi belirledigimiz hatayi dondurebiliyoruz. Default
																							// olarak 400 firlaticakken biz guncellemeler ile 401 firlatmasini saglariz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
		String base64encoded = authorization.split("Basic ")[1]; // consoleda da gozuktugu gibi sifrelenmis kismin sol
																	// tarafindaki fazlaligi atariz
		String decoded = new String(Base64.getDecoder().decode(base64encoded)); // decode ederiz
		String[] parts = decoded.split(":");// [ "user1","Password1"]
		System.out.println("Auth a girdi ------------");
		String username = parts[0];
		String password = parts[1];
		User inDB = userRepository.findByUsername(username);
		if (inDB == null) {
			ApiError error = new ApiError(401, "Unauthorized request", "/api/1.0//auth");// boyle bir user olmadiginda 401 gondeririz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
		String hashedPassword = inDB.getPassword();
//		if(!passwordEncoder.matches(password, hashedPassword)) {
//			ApiError error = new ApiError(401, "Unauthorized request", "/api/1.0//auth");
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//			
//		}
		return ResponseEntity.ok(inDB);
	}

}
