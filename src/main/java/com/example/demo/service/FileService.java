package com.example.demo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.FileAttachment;
import com.example.demo.repository.FileAttachmentRepository;

@Service
@EnableScheduling
public class FileService {
	
	@Value("${upload-path}")
	String uploadPath;
	
	@Autowired
	FileAttachmentRepository fileAttachmentRepository;
	
	
	
	public String writeBas64EncodedStringToFile(String image) throws IOException {//frontendden gelen gorseli targete gonderip dosya olarak kaydederiz random urettigimiz ismi doneriz
		
		
		
		String fileName =  generateRandomName();//gorselin ismini random olarak olustururuz
		
		File target = new File(uploadPath+"/"+fileName);
		
		OutputStream outputStream = new FileOutputStream(target);//bu file'i olusturup yazabilmek icin buna ihtiyacimiz var
		
		byte[] base64encoded = Base64.getDecoder().decode(image);//outputStream byte array istedigi icin donusturme yapariz

	
		
		outputStream.write(base64encoded);
		outputStream.close();
		
		return fileName;
		
	}
	
	
	public String generateRandomName() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	

	public void deleteFile(String oldImageName) {
		
		if(oldImageName==null) {//Eger kullanici ilk kez fotograf yukluyorsa oldImageName null'dir.Nullpointer yememek icin kontrol ederiz.
			return;
		}
		try {
			Files.deleteIfExists(Paths.get(uploadPath,oldImageName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String detectType(String value) {

		Tika tika = new Tika() ;//tika kullanarak userin gonderdigi gorsel tipini gorebiliriz

		byte[] base64encoded = Base64.getDecoder().decode(value);//tika byte array istedigi icin donusturme yapariz

		

		return tika.detect(base64encoded);
	}
	
	public FileAttachment saveAttachment(MultipartFile multipartfile) { //tweet olarak gelen goreseli targeta gonderir

		String fileName =  generateRandomName();//gorselin ismini random olarak olustururuz
		
		File target = new File(uploadPath+"/"+fileName);//yazilacak yer adres veririz
		
		OutputStream outputStream;
		
		try {
			outputStream = new FileOutputStream(target);
			outputStream.write(multipartfile.getBytes());//gelen file'on byte kodlarini outputStream'e veririz, gerekli dizine yazabilmesi icin
			outputStream.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		FileAttachment attachment = new FileAttachment(); //tweet ile gelen gorseller icin attachment objesi olusturulur ve bu obje doner
		attachment.setName(fileName);
		attachment.setDate(new Date());
		
		return fileAttachmentRepository.save(attachment); //olusturdugumuz attachment onjesini save eder ve response olarak o objeyi doneriz
		
	}

	@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
	public void cleanupStorage() {//bu fonksiyon 24saatte bir sistemdeki herhangi bir tweete bagli olmayan gorselleri siler
		Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 10000));
		
		List<FileAttachment> filesToBeDeleted = fileAttachmentRepository.findByDateBeforeAndTwitIsNull(twentyFourHoursAgo); //24 saatten eski olan ve herhangi bir twite bagli olmayan resimleri aliriz
		
		for(FileAttachment file : filesToBeDeleted) {
			deleteFile(file.getName()); //burada belirledigimiz klasorden siler
			fileAttachmentRepository.deleteById(file.getId());//burada databaseden siler
		}
		
	}


	
	
	
	
	
	
	

}