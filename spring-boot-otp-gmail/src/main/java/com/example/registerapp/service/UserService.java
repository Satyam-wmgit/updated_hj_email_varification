package com.example.registerapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.registerapp.dto.LoginDto;
import com.example.registerapp.dto.RegisterDto;
import com.example.registerapp.entity.User;
import com.example.registerapp.repository.UserRepository;
import com.example.registerapp.util.EmailUtil;
import com.example.registerapp.util.OtpUtil;

import jakarta.mail.MessagingException;

@Service
public class UserService {

	@Autowired
	private OtpUtil otpUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String register(RegisterDto registerDto) {
		Optional<User> users = userRepository.findByEmail(registerDto.getEmail());
		if (users.isPresent()) {
			String mail = users.get().getEmail();
			if (mail.equals(registerDto.getEmail())) {
				System.out.println(mail.equals(registerDto.getEmail()));

				return " user already registered";
			}
		}

		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send otp please try again");
		}

		User user = new User();

		user.setFirstName(registerDto.getFirstName());
		user.setLastName(registerDto.getLastName());
		user.setPhone(registerDto.getPhone());
		user.setDefaultSchoolId(registerDto.getDefaultSchoolId());
		user.setHeight(registerDto.getHeight());
		user.setWeight(registerDto.getWeight());
		user.setEmail(registerDto.getEmail());

		// user.setPassword(registerDto.getPassword());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		user.setConfirmPassword(null); // for not creating column in table

		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		// for password validation
		if (registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
			userRepository.save(user);

			return user + "  Email sent... please verify account within 1 minute";

		} else {
			return "password does not match";

		}
	}

	public String verifyAccount(String email, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		if (user.getOtp().equals(otp)
				&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
			user.setTextable(true);
			userRepository.save(user);
			return user + "OTP verified you can login";
		} else if (!user.getOtp().equals(otp))
			return "OTP is incorrect";

		return "Please regenerate otp and try again";
	}

	public String regenerateOtp(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(email, otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send otp please try again");
		}
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		return user + " Email sent... please verify account within 1 minute";
	}

	public String login(LoginDto loginDto) {

		Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
		if (user.isEmpty()) {
			return "user not found with this email";
		}
		/*
		 * if (!loginDto.getPassword().equals(user.get().getPassword())) { return
		 * "Password is incorrect"; }
		 */
		if(!passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
			return "password is incorrect";
		}
		
		else if (!user.get().isTextable()) {
			return "your account is not verified";
		}
		return "Login successful";
	}
}
