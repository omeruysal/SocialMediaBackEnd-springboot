package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //bu exceptionu olusturdugumuzda response cevabi not found donecek
public class NotFoundException extends RuntimeException {//olmayan bir user olmadiginda spring 500 doner. Biz 404 donmesini isteriz ve bu class bu yuzden var

}//getByUsername icersinde kullaniriz
