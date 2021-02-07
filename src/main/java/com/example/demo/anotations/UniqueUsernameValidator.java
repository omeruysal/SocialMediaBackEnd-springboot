package com.example.demo.anotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{//anotasyon adi ve hangi turlerde kullanilicagini veririz
	
	

	@Autowired
	UserRepository userRepository;
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) { //bu fonksiyon sayesinde usernamein unique olmasini saglariz. 
																				//	Bu yontem yerine Column anotasyonun unique ozelliginide kullanabilirdik fakat 
																				//o durum da diger hatalar ile cakisma durumu olurdu. Ornegi kullanici olan bir user ismini kullandi ve display name i bos birakti
																				// @Notnull @size gibi anotasyonlarla ilgili validation islemleri UserControllera gelmeden once yapiliyor. unique ozelligi icin veritabanina gitmesi lazim.
																				//Bizim senaryomuzda displayname bos oldugu icin validator database gitmeden hata firlatiyor ve username'in unique olup olmadigi kontrol edilemiyor.
		
		User user =userRepository.findByUsername(username);
		if(user !=null) {
			return false;
		}
		
		return true;
	}

}
