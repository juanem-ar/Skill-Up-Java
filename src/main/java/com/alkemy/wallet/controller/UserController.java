package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserListDto;
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
import org.springframework.web.bind.annotation.*;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final IUserService userService;

    @Operation(method = "DELETE", summary = "deleteUser", description = "Delete user using id, by authenticated user or admin user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_USER","ROLE_ADMIN" })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) throws Exception{
        return new ResponseEntity<>(userService.deleteUser(id,authentication), HttpStatus.OK);
    }

    @Operation(method = "GET", summary = "findAllUsers", description = "Get all system users. Only use by admin.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("/all")
    public ResponseEntity<ResponseUserListDto> findAllUsers (
    	@RequestParam(name = "page") Integer page, HttpServletRequest httpServletRequest) throws Exception {
    	return ResponseEntity.ok(userService.findAllUsers(page, httpServletRequest));
    }


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

    @Operation(method = "PATCH", summary = "updateUserDetails", description = "Edit first name and last name by user authenticated.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDetailsUserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDetailsUserDto> updateUserDetails(@PathVariable Long id, @Valid @RequestBody PatchRequestUserDto dto,
                                                                    Authentication authentication) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserDetails(id, dto, authentication));
    }
}
