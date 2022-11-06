package com.alkemy.wallet.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequestDto {
    @NotNull(message = "{user.email.empty}")
    @Email
    @Column(unique = true)
    private String email;
    @NotNull
    @Length(min = 6)
    private String password;
}
