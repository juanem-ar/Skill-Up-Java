package com.alkemy.wallet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.alkemy.wallet.service.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;

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
/*
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
    	HttpServletRequest httpServletRequest) throws Exception {
    	return ResponseEntity.ok(userService.findAllUsers(
    		page, 
    		httpServletRequest));
    }
    */
    @Operation(method = "GET", summary = "getUserDetails", description = "Get user detail. Authentication needed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDetailsUserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping
    public ResponseEntity<ResponseDetailsUserDto> getUserDetails(Authentication authentication) throws Exception {
		return ResponseEntity.ok(userService.getUserDetail(authentication));
    }

    @Operation(method = "GET", summary = "getUserDetailsById", description = "Get user detail by admin role",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDetailsUserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailsUserDto> getUserDetailsById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getUserDetailById(id));
    }
    /*
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDetailsUserDto> updateUserDetails(
    	@PathVariable Long id,
    	@RequestBody PatchRequestUserDto dto,
    	@RequestHeader(name = "Authorization") String token) throws Exception {
		return ResponseEntity.ok(
			userService.updateUserDetails(id, dto, token));
    }*/
}
