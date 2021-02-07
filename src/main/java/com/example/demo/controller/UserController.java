package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.anotations.CurrentUser;
import com.example.demo.error.ApiError;
import com.example.demo.model.User;
import com.example.demo.model.userVM.UserUpdateVM;
import com.example.demo.model.userVM.UserVM;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/api/1.0")
public class UserController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	
	
	
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) { // user objesi bu parametreye dusmeden once bir validation logic'inden gecer

		
		userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/users")												//CurrentUser ile login olmus user bilgisini aliriz
	public Page<UserVM> getUsers(Pageable page, @CurrentUser User user) {//url'den size ve page parametrelerini alir.UserRepositoryde page objesini parametre olarak veririz
	
		return userService.getUsers(page, user).map(UserVM::new); //map(UserMV::new) diyerekte listedeki her elemani UserVm constructoruna firlatabiliriz
	}
	
	@GetMapping("/users/{username}")
	public UserVM getUser(@PathVariable String username) {
		return new UserVM((User) userService.getByUsername(username));
	}
	
	@PutMapping("/users/{username}")
	@PreAuthorize("#username == #loggedInUser.username")//Spring Expression Language - SpEL //Parametrelerden gelen degerleri ayni isimler ile kullaniriz // parantez icinde ki kosul saglanmaz ise forbidden doner.Kisaca  user'in kendisinden baska bir user icin update yapabilmesini engelleriz
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateVM updatedUser, @PathVariable String username, @CurrentUser User loggedInUser) throws IOException {
//		if(!loggedInUser.getUsername().equals(username)) {  //kontrolu kendimiz de yapabiliriz ya da Spel ile springe de yaptirabiliriz
//			ApiError error = new ApiError(403,"Cannot change another users data", "/api/1.0/users"+username);
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
//		}
		return ResponseEntity.ok(new UserVM((User)userService.updateUser(username, updatedUser)));
	}
	
	
	
	
	
	
	
	
	
	@PatchMapping("/users/{username}")
	public UserVM updateFiledOfUser(@PathVariable String username,@RequestBody User us) {
		
	
	
		return new UserVM((User)userService.updatePatchExample(username, us.getDisplayName()));
	}
	
	//spring validation sayesinde belirttigimiz hatalari boylede yakalayabiliriz.Bu kismi commente aldiktan sonra otomatik olarak bizim olusturdugumuz errorHandlera duser. Bu sekilde ayri olarakta handle edebiliriz fakat belli bir standart olusturmak icin ErrorHandler classinda yapmaya basladik.
	//@ExceptionHandler(MethodArgumentNotValidException.class)									//MethodArgumentNotValidException hatasi firlatildiginda burdaki fonksiyona girecek
//	@ResponseStatus(HttpStatus.BAD_REQUEST)														//hicbirsey belirtmezsek otomatik olarak 200 doner, bu yuzden bad request oldugunu belirtiriz
//	public ApiError handleValidationException(MethodArgumentNotValidException exception) {
//		ApiError error = new ApiError(400, "Validation error", "/api/1.0/users");
//		
//		Map<String, String> validationErrors = new HashMap<>();
//		
//		for(FieldError fieldError: exception.getBindingResult().getFieldErrors()) { 			//Valid sayesinde model sinifinda kullandigimiz anotasyonlarin izin vermedigi degerler gelince o degerleri ve degerlere ozel mesajlari map icine yerlestiririz. Bu mapi reacta gondeririz.
//			System.out.println("field : ---"+fieldError.getField()+" ----- message : ----"+fieldError.getDefaultMessage());
//			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
//		}
//		
//		error.setValidationErrors(validationErrors);
//		return error; // error nesnemizi dondururuz. Bu error nesnesi react tarafinda yakalanir.
//	}

}
