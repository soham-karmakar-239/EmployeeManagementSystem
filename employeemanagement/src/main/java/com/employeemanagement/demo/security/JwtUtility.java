package com.employeemanagement.demo.security;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for JWT
 * 
 * @author 2144388
 *
 */
@Component
public class JwtUtility {

	/**
	 * Auto-generated security key for cryptographic signature
	 */
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	/**
	 * Builds new token from Authentication details
	 * 
	 * @param authentication - Authentication
	 * @return created JWT
	 */
	public String createToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + 3600000);

		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	/**
	 * Gets bearer token from HttpRequest
	 * 
	 * @param request - HttpRequest object
	 * @return resolved JWT
	 */
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	/**
	 * Validates authentication token received with request
	 * 
	 * @param token - JWT
	 * @return Validation result
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Gets username from authentication token
	 * 
	 * @param token - JWT
	 * @return Username
	 */
	public String getUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

}
