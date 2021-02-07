package com.example.demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.FileAttachment;
import com.example.demo.service.FileService;

@RestController
public class FileController {
	
	@Autowired
	FileService fileService;
	
	@PostMapping("/api/1.0/twit-attachment")
	public FileAttachment saveTwitAttachment(MultipartFile file) {
		
		return fileService.saveAttachment(file);
		
		
	}

}
