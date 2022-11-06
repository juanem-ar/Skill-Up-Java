package com.alkemy.wallet.dto;

import java.sql.Timestamp;
import java.util.List;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Role role;
	private List<Account> accounts;
	private Timestamp creationDate;
	private Timestamp updateDate;
	private Boolean deleted = Boolean.FALSE;
}
