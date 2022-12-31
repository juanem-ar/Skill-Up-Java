package com.alkemy.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchRequestUserDto {
	@NotNull
	@Size(min = 3, max = 50)
	@Schema(required = true, example = "McKein", description = "User lastname", minLength = 3, maxLength = 50)
	private String firstName;
	@NotNull
	@Size(min = 3, max = 50)
	@Schema(required = true, example = "McKein", description = "User lastname", minLength = 3, maxLength = 50)
	private String lastName;

}
