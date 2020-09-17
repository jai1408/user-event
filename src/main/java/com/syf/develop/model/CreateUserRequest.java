package com.syf.develop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

	@NotBlank
	private String name;
	@Email
	private String email;
	@NotBlank
	@Size(min = 8, max = 80, message = "Password should be between 8 and 80 characters")
	private String password;

}
