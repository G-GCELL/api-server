package com.gabia.weat.gcellapiserver.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.dto.OauthDto.OauthLoginResponseDto;
import com.gabia.weat.gcellapiserver.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping(value = "/oauth2/authorization/hiworks")
	public ResponseEntity<ApiResponseDto> redirectHiworksAuth() {
		URI hiworksAuthUri = authService.getHiworksAuthUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(hiworksAuthUri);
		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).body(ApiResponseDto.success());
	}

	@GetMapping(value = "/login/oauth2/code/hiworks")
	public ResponseEntity<ApiResponseDto> oauthLogin(@RequestParam("auth_code") String authCode) {
		OauthLoginResponseDto oauthLoginResponseDto = authService.oauthLogin(authCode);
		return ResponseEntity.ok(ApiResponseDto.success(oauthLoginResponseDto));
	}

}
