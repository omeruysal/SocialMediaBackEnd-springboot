package com.example.demo.anotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.service.FileService;

public class ProfileImageValidator implements ConstraintValidator<ProfileImage, String> {

	@Autowired
	FileService fileService;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null|| value.isEmpty()) { // bu kosul resim gelmeden sadece displayname updatei gelince calisacaktir.Bu kosul olmadan resim geldiginde nullpointerExp firlatir
			return true;
		}
		String fileType = fileService.detectType(value);
		
		if(fileType.equalsIgnoreCase("image/jpeg")||fileType.equalsIgnoreCase("image/png")) {
			
			return true;
		}
		return false;
	}

}
