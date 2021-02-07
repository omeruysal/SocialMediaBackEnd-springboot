package com.example.demo.error;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //bu anotasyon ile null olan degerleri bodye eklemeyiz bu sayede null degerler response bodyde gozukmez
public class ApiError {

	
	private int status;

	
	private String message;

	
	private  String path;

	
	private Map<String, String> validationErrors;
	
	public ApiError(int status, String message, String path) {
		this.status = status;
		this.message = message;
		this.path = path;
	}

}
