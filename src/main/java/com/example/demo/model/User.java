package com.example.demo.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.anotations.UniqueUsername;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@Entity
public class User implements UserDetails { //spring security user classimizin UserDetails implement etmesini istemektedir. loadByUsername methodu userDetails dondugu icin impelement etmemiz gerekir.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message="Username is mandatory")
	@Size(min =4,max=25,message="Size must be between 4 and 25")
	@UniqueUsername
	private String username;
	
	@NotBlank(message="Display name is mandatory")
	@Size(min =4,max=25,message="Size must be between 4 and 25")
	private String displayName;
	
	@NotBlank(message="Password  is mandatory")
	@Size(min =8,message="Password size must be minumum 8")
	@Pattern(regexp= "^(?=.*[a-z])(?=.*[a-z])(?=.*\\d).*$",message="Password must have at least 1 uppercase, 1 lowercase letter and 1 number.")
	private String password;
	
	@Lob//Large object olucagini belirtiriz
	private String image;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return AuthorityUtils.createAuthorityList("Role_user"); //farkli role tipleri icin kullanilir
	}

	@Override
	public boolean isAccountNonExpired() {//burdan itibaren projede bulunmayan ozellikler oldugu icin sadece true doneriz 
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
	

}
