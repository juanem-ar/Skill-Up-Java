package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.messageCostumerErros.ErrorsResponseMessage;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IFixedDepositService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.Period;

@RequestMapping("/fixedDeposit")
@RestController
public class FixedTermDepositController {
    @Autowired
    private IFixedDepositService fixedDepositService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<String> createFixedDeposit(HttpServletRequest req, @Valid @RequestBody FixedDepositDto dto) throws Exception {
        String token = req.getHeader("Authorization");
        String jwt = jwtUtils.getJwt(token);
        return ResponseEntity.status(HttpStatus.OK).body(fixedDepositService.addFixedDeposit(jwtUtils.extractUsername(jwt), dto));
    }
}
