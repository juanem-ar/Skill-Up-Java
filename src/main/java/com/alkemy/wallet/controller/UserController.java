package com.alkemy.wallet.controller;


import com.alkemy.wallet.service.IUserService;

import lombok.AllArgsConstructor;

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
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;

import java.util.List;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {
    private IUserService userService;

    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUsers(@PathVariable Long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
    
    
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> findAllUsers () {
    	return ResponseEntity.ok(userService.findAllUsers());
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUserDetails(
    	@RequestHeader(name = "Authorization") String token,
    	@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserDetails(id, token));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUserDetails(
    	@PathVariable Long id,
    	@RequestBody PatchRequestUserDto dto,
    	@RequestHeader(name = "Authorization") String token){
		return ResponseEntity.ok(
			userService.updateUserDetails(id, dto, token));
    }
}
