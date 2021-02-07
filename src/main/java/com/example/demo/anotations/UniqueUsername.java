package com.example.demo.anotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { UniqueUsernameValidator.class})//uygulanacak olan validation logic classini veririz
@Target({ FIELD, })
@Retention(RUNTIME)
public @interface UniqueUsername {
	
	String message() default "Username is already choosen";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
