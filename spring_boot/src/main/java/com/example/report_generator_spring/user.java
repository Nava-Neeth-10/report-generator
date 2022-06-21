package com.example.report_generator_spring;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class user {
	@Id
	Integer id;
	String first_name,last_name,email,username,password;
	
	public void set_user(int id,String first_name,String last_name, String email,String username, String password)
	{
		this.id=id;
		this.first_name=first_name;
		this.last_name=last_name;
		this.email=email;
		this.username=username;
		this.password=password;
	}
}
