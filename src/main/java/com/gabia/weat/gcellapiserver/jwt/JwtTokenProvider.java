package com.gabia.weat.gcellapiserver.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellapiserver.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final JwtProperty property;
	private final CustomUserDetailsService userDetailsService;
	private static final String ROLE_CLAIM = "roles";

	public String generateAccessToken(String email, Collection<? extends GrantedAuthority> authorities) {
		long now = (new Date()).getTime();
		Date accessTokenExpiredTime = new Date(now + property.getAccessTokenExpiration());
		String roles = authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		return Jwts.builder()
			.setSubject(email)
			.setExpiration(accessTokenExpiredTime)
			.claim(ROLE_CLAIM, roles)
			.signWith(getSignInKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		List<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(ROLE_CLAIM).toString().split(","))
				.map(SimpleGrantedAuthority::new).toList();
		UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(property.getSecret());
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSignInKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

}
