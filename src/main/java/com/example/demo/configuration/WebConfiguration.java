package com.example.demo.configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
		
		@Value("${upload-path}")
		String uploadPath;
		
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) { //gorsel icin gonderilen requestlerin handle edilmesi ve ayri bir dizin olarak erisilebilmesi icin yapariz
			
			registry.addResourceHandler("/images/**") //seklinde gelen requestleri icin burdaki handler devreye girecek
			.addResourceLocations("file:./"+uploadPath+"/")// ve solda verdigimiz tarafta bulmaya calisicak
			.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)); // verilen surede cachelenmesine izin veririz
		}
				
		@Bean
		CommandLineRunner createStorageDirectories() { //gerekli kontroller yapildiktan sonra eger picture-storage adli klasor yoksa proje baslarken olusturulur
			return (args) -> { 
				File folder = new File(uploadPath);
				boolean folderExist = folder.exists()&& folder.isDirectory();
				if(!folderExist) {
					folder.mkdir();
				}
			};
			
		}
}
