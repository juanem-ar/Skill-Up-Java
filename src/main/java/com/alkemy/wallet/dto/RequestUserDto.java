package com.alkemy.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {

    @Size(min = 3, max = 50)
    @NotNull
    @Schema(required = true, example = "Jhon", description = "User name", minLength = 3, maxLength = 50)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 50)
    @Schema(required = true, example = "McKein", description = "User lastname", minLength = 3, maxLength = 50)
    private String lastName;

    @NotNull
    @Email
    @Size(min = 6)
    @Schema(format = "email", example = "jhonMcKein@hotmail.com", minLength = 6, description = "User email")
    private String email;

    @NotNull
    @Length(min = 8, max = 25)
    @Schema(required = true, example = "12345678", description = "Password", minLength = 8, maxLength = 25)
    private String password;

    @Schema(required = true, example = "USER")
    private String role;
}
