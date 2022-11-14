package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {

    @Size(min = 3, max = 50)
    @NotNull
    private String firstName;
    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;
    @NotNull
    @Email
    @Size(min = 6)
    private String email;
    @NotNull
    @Length(min = 8)
    private String password;
    private Role role;
}
