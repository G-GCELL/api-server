package com.gabia.weat.gcellapiserver.oauth2;

import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gabia.weat.gcellapiserver.dto.OauthDto.HiworksUserInfoResponseDto;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HiworksOauth2Client {

	private final RestTemplate restTemplate = new RestTemplate();
	private final OAuth2ClientProperties oAuth2ClientProperties;
	private static final String AUTHORIZATION_TYPE = "Bearer ";
	private static final String PROVIDER_AND_REGISTRATION = "hiworks";
	private static final String ACCESS_TYPE = "online";
	private static final String EMAIL_FORMAT = "@gabia.com";

	public HiworksUserInfoResponseDto getUserInfo(String authCode) {
		String hiworksAccessToken = requestAccessToken(authCode);
		Map<String, Object> userInfoResponse = requestUserInfo(hiworksAccessToken);
		String userId = userInfoResponse.get("user_id") + EMAIL_FORMAT;
		String name = (String)userInfoResponse.get("name");

		return HiworksUserInfoResponseDto.builder()
			.hiworksEmail(userId)
			.userName(name)
			.build();
	}

	public String getAuthorizationUri() {
		return String.valueOf(oAuth2ClientProperties.getProvider().get(PROVIDER_AND_REGISTRATION).getAuthorizationUri());
	}

	private String requestAccessToken(String authCode) {
		String requestUri = oAuth2ClientProperties.getProvider().get(PROVIDER_AND_REGISTRATION).getTokenUri();
		String clientId = oAuth2ClientProperties.getRegistration().get(PROVIDER_AND_REGISTRATION).getClientId();
		String clientSecret = oAuth2ClientProperties.getRegistration().get(PROVIDER_AND_REGISTRATION).getClientSecret();
		String grantType = oAuth2ClientProperties.getRegistration().get(PROVIDER_AND_REGISTRATION).getAuthorizationGrantType();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("grant_type", grantType);
		params.add("auth_code", authCode);
		params.add("access_type", ACCESS_TYPE);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<Map> response = restTemplate.postForEntity(requestUri, request, Map.class);

		if (!(response.getBody().get("code").equals("SUC"))) {
			throw new CustomException(ErrorCode.HIWORKS_ACCESS_TOKEN_REQUEST_ERROR);
		}
		Map<String, Object> data = (Map<String, Object>)response.getBody().get("data");
		String accessToken = (String)data.get("access_token");

		return accessToken;
	}

	private Map<String, Object> requestUserInfo(String accessToken) {
		String requestUri = oAuth2ClientProperties.getProvider().get(PROVIDER_AND_REGISTRATION).getUserInfoUri();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE + accessToken);

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<Map> response = restTemplate.exchange(requestUri, HttpMethod.GET, request, Map.class);
		Map<String, Object> userInfo = response.getBody();

		return userInfo;
	}

}