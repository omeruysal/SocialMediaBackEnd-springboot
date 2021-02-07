package com.example.demo.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


//Standart bir hata modellemesi icin bu adimlar yapilmistir.Zorunlu degildir.Tanimlama yapmaz isek default hatalar response olarak gonderilir.Fakat bu sekilde client tarafinda belirli bir duzen olustururuz
//Bu classi BasicErrorController yerine kendi custom error controllerimizi kullanmak icin yaratiyoruz.User password gibi kisimlarda yasanan problemler sonucu firlatilacak hatalari kendi hatalarimiz ile degistirebilmek icin
//Login signup hatalarida buraya duser. UserControllerda tanimladigimiz eski validation fonksiyonunu disable yaptiktan sonra o hatalar default olarak buraya duser
@RestController // /error pathini dinleyen bir controller tanimlamamiz gerekir. Bu sinifi ayni zamanda controller icin kullaniriz. /error ile ilgili hersey buraya duser
public class ErrorHandler implements ErrorController{ 
	
	//requestten getErrorAttributes yapmamiz gerek ve bunu response haline getiricez. getErrorAttributes icin ErrorAttributes a ihtiyacimiz var. Kisacasi errorlarla ilgili bilgilere ulasabilmek icin ErrorAttributes enjekte etmemiz gerekir
	@Autowired
	private ErrorAttributes errorAttributes;
	
	@RequestMapping("/error")//post get vs request tipinden bagimsiz tum requestler icin calisan method yazmamiz gerekir. Bu yuzden requestmapping kullaniriz
	public ApiError handleError(WebRequest webRequest) {
		
		Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(Include.MESSAGE, Include.BINDING_ERRORS)); // asagida binding errors ve message kullandigimiz icin onlari implement ederiz
																						// bu attributes mapi icinde timestamp message error status gibi alanlar var. Tamamini gormek icin DefauktErrorAttributes classina gidebiliriz
		String message = (String) attributes.get("message");
		String path = (String) attributes.get("path");
		int status = (Integer) attributes.get("status");
														//errorAttributes'tan error degerlerini aliriz ve kendi objemize aktarip o objeyi dondururuz.
		ApiError error = new ApiError(status, message, path);
		System.out.println(error.getStatus()+ " : error/ 'dan geldi");
		
		if(attributes.containsKey("errors")) { //eger spring  herhangi bir error firlatirsa error keyowrdu var demektir.Bu erroru/errorlari kendi mapimize koyup frontend'e gondeririz. // bu kismi if icine aldik cunku error olmadigi bir durumda sadece yablis username girildiginde de(login sayfasinda ornegin) spring /error dizinine gonderir ve boylece attributes.getErrorstan null doner ve nullpointerexception firlatir
			
			List<FieldError> fieldErrors = (List<FieldError>) attributes.get("errors");
			Map<String, String> validationErrors = new HashMap<>();
			for(FieldError fieldError:fieldErrors){
				validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
				
			}
			error.setValidationErrors(validationErrors);
			
		}
		 
		 
		 return error;
	}
	
	
	@Override
	public String getErrorPath() {//error pathinin ne oldugunu soylememiz gerekir
		
		return "/error";
	}
	
	

}
