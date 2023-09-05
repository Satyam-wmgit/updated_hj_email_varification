package com.example.registerapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "user11")
public class User {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	// private String name;
	@Column(name = "firstname", length = 100)
	private String firstName;
	@Column(name = "lastname", length = 100)
	private String lastName;
	@Column(name = "phone", length = 20)
	private String phone;
	@Column(name = "defaultschoolid")
	private Integer defaultSchoolId;
	@Column(name = "height")
	private Integer height;

	@Column(name = "weight")
	private Double weight;
	private String email;
	private String password;
	//@Column(name = "confirm_password", length = 40)
	@Transient
	private String confirmPassword;
	@Column(name = "wid")
	private String wid = "hj-fl-tampa";
	private boolean textable;
	private String otp;
	private LocalDateTime otpGeneratedTime;
}
