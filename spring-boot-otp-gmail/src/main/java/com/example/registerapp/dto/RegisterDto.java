package com.example.registerapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String password;
	private String confirmPassword;
	private Integer defaultSchoolId;
	private Integer height;
	private Double weight;
	private String wid ="hj-fl-tampa";

}
