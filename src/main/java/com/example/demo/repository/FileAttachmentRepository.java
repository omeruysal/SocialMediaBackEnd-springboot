package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.FileAttachment;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
	
	List<FileAttachment> findByDateBeforeAndTwitIsNull(Date date);

}
