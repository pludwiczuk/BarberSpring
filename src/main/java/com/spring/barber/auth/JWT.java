package com.spring.barber.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWT {

	@Value("${jwt.secret}")
  private String secret;
	
	@Value("${jwt.expirationTimeMinutes}")
	private int jwtExpirationMinutes;

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
    if (roles.contains(new SimpleGrantedAuthority("ROLE_BUSINESS_USER"))) {
			claims.put("isBusinessUser", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			claims.put("isUser", true);
		}
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
      .setClaims(claims)
      .setSubject(subject)
      .setIssuedAt(new Date(System.currentTimeMillis()))
		  .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMinutes * 60 * 1000))
      .signWith(SignatureAlgorithm.HS512, secret)
      .compact();
	}

	public boolean validateToken(String token, HttpServletRequest httpServletRequest) {
    try {
    	Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    	return true;
    }catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			httpServletRequest.setAttribute("invalid", ex.getMessage());
    }catch (ExpiredJwtException ex){
      httpServletRequest.setAttribute("expired", ex.getMessage());
		}
    return false;
	}

	public String extractUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public List<SimpleGrantedAuthority> extractRoles(String authToken) {
		List<SimpleGrantedAuthority> roles = null;
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isUser = claims.get("isUser", Boolean.class);
		Boolean isBusinessUser = claims.get("isBusinessUser", Boolean.class);
		if (isAdmin != null && isAdmin == true) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		if (isUser != null && isUser == true) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		}
		if (isBusinessUser != null && isBusinessUser == true) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_BUSINESS_USER"));
		}
		return roles;
	}

}
