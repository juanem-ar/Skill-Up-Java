package com.alkemy.wallet.security.service;

import com.alkemy.wallet.repository.IUserRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Service
public class JwtUtils implements IJwtUtils {
    private String SECRET_KEY = "secret";
    @Autowired
    private IUserRepository userRepository;

    @Override
	public String extractUsername (String token){ return extractClaim(token, Claims::getSubject);}
    @Override
	public Date extractExpiration(String token){ return extractClaim(token, Claims::getExpiration);}
    @Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    @Override
	public String getJwt(String token){
        String jwt = token.substring(7);
        return jwt;
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token){ return extractExpiration(token).before(new Date());}

    @Override
	public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userRepository.findByEmail(userDetails.getUsername()).getId());
        return createToken(claims, userDetails.getUsername());
    }
    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 100 * 60 * 60 * 10))//el ultimo numero tenia 10, le puse 100; calculo para q el token dure 10 horas.
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    @Override
	public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	@Override
	public Long extractUserId(String token) {
		// TODO Auto-generated method stub
		return null;
	}
}
