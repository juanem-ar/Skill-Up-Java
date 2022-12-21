package com.alkemy.wallet.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {

    @NotNull
    @Email
    @Size(min = 6)
    @Schema(format = "email", example = "juanem@hotmail.com", minLength = 6, description = "User email")
    private String email;

    @NotNull
    @Length(min = 8, max = 25)
    @Schema(required = true, example = "12345678", description = "Password", minLength = 8, maxLength = 25)
    private String password;
}
