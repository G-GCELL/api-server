package com.gabia.weat.gcellapiserver.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.dto.OauthDto.OauthLoginResponseDto;
import com.gabia.weat.gcellapiserver.jwt.JwtProperty;
import com.gabia.weat.gcellapiserver.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	@Value("${redirect-page-url}")
	private String redirectPageUrl;
	private final AuthService authService;
	private final JwtProperty jwtProperty;

	@GetMapping(value = "/oauth2/authorization/hiworks")
	public ResponseEntity<ApiResponseDto> redirectHiworksAuth() {
		URI hiworksAuthUri = authService.getHiworksAuthUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(hiworksAuthUri);
		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).body(ApiResponseDto.success());
	}

	@GetMapping(value = "/login/oauth2/code/hiworks")
	public ResponseEntity<ApiResponseDto> oauthLogin(@RequestParam("auth_code") String authCode) throws
		URISyntaxException {
		OauthLoginResponseDto oauthLoginResponseDto = authService.oauthLogin(authCode);
		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(setTokenCookieHeader(oauthLoginResponseDto)).build();
	}

	private HttpHeaders setTokenCookieHeader(OauthLoginResponseDto oauthLoginResponseDto) throws URISyntaxException {
		System.out.println(oauthLoginResponseDto.accessToken());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(redirectPageUrl));
		ResponseCookie cookie = ResponseCookie.from("token", oauthLoginResponseDto.accessToken())
			.maxAge(jwtProperty.getAccessTokenExpiration())
			.path("/")
			.httpOnly(true)
			.build();
		headers.set(HttpHeaders.SET_COOKIE, cookie.toString());
		return headers;
	}

}