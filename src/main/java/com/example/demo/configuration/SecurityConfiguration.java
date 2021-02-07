package com.example.demo.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity //bu anotasyon sayesinde global security saglariz ve sinifmizin gecerli olmasini saglariz. Eger bu anotasyonu koymazsak default ayarlar gecerli olur
@EnableGlobalMethodSecurity(prePostEnabled = true)//yarattigimiz webservis methodlarindan hemen once gerceklesek spel{spring expression language) aktiflestirmek icin true sekilde enable etmeliyiz
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserAuthService userAuthService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // cross site request forgery // server tarafindan gonderilen token kontrolu icin ve suan ihtiyacimiz olmadigi icin disable ederiz
		
		http.httpBasic().authenticationEntryPoint(new AuthEntryPoint()); //burada olusturdugumuz classin objesini doneriz ve class icinde browserda otomatik olusan basic authenticationEntryPoint disable yapariz. Bunu yapmazsak tarayicida bize ayrica popup olarak bilgileri sorar
		
		
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/1.0/auth").authenticated() //.authenticated fonksiyonu ile verilen dizine post ile gelen isteklerin authentication credentialslarini barindirmasi gerektigini soyleriz
		.antMatchers(HttpMethod.PUT, "/api/1.0/users/{username}").authenticated()//userlar update yapabilir
		.antMatchers(HttpMethod.POST, "/api/1.0/twites").authenticated() //sadece user tweet atabilir
		.antMatchers(HttpMethod.POST, "/api/1.0/twit-attachment").authenticated() //sadece user goresel upload edebilir
		.and()
		.authorizeRequests().anyRequest().permitAll(); // diger requestler icin izin vermesini soyleriz
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // session createi stateless yapmazsak gonderdigimiz credentialleri bir kere sorar ve cookiye koyar. Fakat biz her istek icin credential sormasini isteriz.
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userAuthService).passwordEncoder(new BCryptPasswordEncoder()); // Spring securitye sunu soyleriz; eger bir user bulmaya calisiyorsan benim servisimi kullan veritabanina erismek icin
	
	}


}
  