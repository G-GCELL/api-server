package com.gabia.weat.gcellapiserver.dto;

import lombok.Builder;

public class OauthDto {

	@Builder
	public record HiworksUserInfoResponseDto(
		String hiworksEmail,
		String userName
	) {

	}

	@Builder
	public record OauthLoginResponseDto(
		String accessToken,
		String name
	) {

	}

}
