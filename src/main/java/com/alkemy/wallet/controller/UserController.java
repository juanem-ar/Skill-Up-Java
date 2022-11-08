package com.alkemy.wallet.controller;


import com.alkemy.wallet.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.wallet.dto.ResponseUserDto;

import java.util.List;
@RequestMapping("api/v1/users")
@RestController
public class UserController {
    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(method = "DELETE", summary = "deleteUsers", description = "Eliminar usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. Se eliminó correctamente el usuario"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUsers(@PathVariable Long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @Operation(method = "GET", summary = "findAllUsers", description = "Traer todos los usuarios.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> findAllUsers () {
    	return ResponseEntity.ok(userService.findAllUsers());
    }
}
