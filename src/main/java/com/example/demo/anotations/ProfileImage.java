package com.example.demo.anotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { ProfileImageValidator.class})//uygulanacak olan validation logic classini veririz
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ProfileImage {
	
	String message() default "Profile image accepts only JPEG and PNG";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
