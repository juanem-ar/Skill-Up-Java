package com.alkemy.wallet.dto;

import java.sql.Timestamp;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.alkemy.wallet.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDetailsUserDto {
  private Long id;
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
  private List<Account> accounts;
  private Timestamp creationDate;
  private Timestamp updateDate;
}
