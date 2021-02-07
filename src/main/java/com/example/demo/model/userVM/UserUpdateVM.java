package com.example.demo.model.userVM;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.anotations.ProfileImage;

import lombok.Data;

@Data
public class UserUpdateVM {
	
	@NotBlank(message="Display name is mandatory")
	@Size(min =4,max=25,message="Size must be between 4 and 25")
	private String displayName;
	
	@ProfileImage
	private String image;
}
