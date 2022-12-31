package com.alkemy.wallet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info =@Info(
				title = "Wallet",
				version = "v2.0",
				contact = @Contact(name = "Juan Manuel López", email = "juanem@hotmail.com"),
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
				//termsOfService = "${tos.uri}",
				description = "Wallet Application by Alkemy DEV Team"
		)
		/*,
		servers = @Server(
				url = "",
				description = ""
		)*/
)
@SecurityScheme(
		name = "Bearer Authentication",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "Bearer"
)
public class WalletApplication {
	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}
}
