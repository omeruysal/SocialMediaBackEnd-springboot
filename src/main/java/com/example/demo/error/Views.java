package com.example.demo.error;

import com.fasterxml.jackson.annotation.JsonView;

public interface Views { //bu class suanda kullanilmamaktadir.
	
	class Base{} 
	// 	@JsonView(Views.Base.class) bu anotasyonu modelimizde istedigimiz degisken uzerine ekleyerek repsponseta gozukmesini engelleyebiliriz
	//auth controllerda response olarak db'deki user'i aynen doneriz.Fakat bu durumda sifre de gozukur.
	//Bunu engellemek icin hangi alanlarin json tarafinda json objesine donusturulebilcegini bu sekilde isarterleriz.
	//(Eger bunlari isaretlemek yerine sadece password field'i uzerinde JsonIggnore kullansaydik bize problem yaraticakti cunku artik ggelen requestlerde de password ignore edilir ve passwordu alamayiz.


}
