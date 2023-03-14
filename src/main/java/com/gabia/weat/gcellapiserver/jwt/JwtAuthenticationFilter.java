package com.gabia.weat.gcellapiserver.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private static final String AUTHORIZATION_HEADER = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		String token = resolveToken((HttpServletRequest)request);
		log.info("---------------- " + token);
		if (token != null && jwtTokenProvider.isTokenValid(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		log.info("---------------- " + authHeader);
		if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
			return null;
		}
		return authHeader.substring(7);
	}

}
