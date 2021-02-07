package com.example.demo.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;



@Target({ElementType.PARAMETER}) //anotasyonun parametre olarak kullanicagini belirtiyoruz
@Retention(RetentionPolicy.RUNTIME) //runtime boyunca kullanilabilcegini belirtiriz
@AuthenticationPrincipal // AuthControllerda parametre olarak Authentication objesi verip,  User user = (User) auth.getPrincipal() islemini yapmak yerine bu islemi spring securitye yaptiririz
public @interface CurrentUser { //biz sadece parametre olarak User user veririz

}
