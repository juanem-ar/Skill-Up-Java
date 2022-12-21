package com.alkemy.wallet.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.alkemy.wallet.service.IUserService;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.dto.ResponseUsersDto;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final IUserService userService;

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
    public ResponseEntity<ResponseUsersDto> findAllUsers (
    	@RequestParam(required = false, name = "page") Integer page,
    	HttpServletRequest httpServletRequest) {
    	return ResponseEntity.ok(userService.findAllUsers(
    		page, 
    		httpServletRequest));
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailsUserDto> getUserDetails(
        @Parameter(description = "id of User to be searched")
    	@RequestHeader(name = "Authorization") String token,
    	@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserDetails(id, token));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDetailsUserDto> updateUserDetails(
    	@PathVariable Long id,
    	@RequestBody PatchRequestUserDto dto,
    	@RequestHeader(name = "Authorization") String token){
		return ResponseEntity.ok(
			userService.updateUserDetails(id, dto, token));
    }
}
