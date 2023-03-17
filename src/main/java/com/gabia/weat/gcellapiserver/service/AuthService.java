package com.gabia.weat.gcellapiserver.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.RoleType;
import com.gabia.weat.gcellapiserver.dto.OauthDto.HiworksUserInfoResponseDto;
import com.gabia.weat.gcellapiserver.dto.OauthDto.OauthLoginResponseDto;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.jwt.JwtTokenProvider;
import com.gabia.weat.gcellapiserver.oauth2.HiworksOauth2Client;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final HiworksOauth2Client hiworksOauth2Client;
	private final MemberRepository memberRepository;

	public URI getHiworksAuthUri() {
		String authorizationUri = hiworksOauth2Client.getAuthorizationUri();
		try {
			return new URI(authorizationUri);
		} catch (URISyntaxException e) {
			throw new CustomException(ErrorCode.URI_SYNTAX_ERROR);
		}
	}

	public OauthLoginResponseDto oauthLogin(String authCode) {
		HiworksUserInfoResponseDto hiworksUserInfoResponseDto = hiworksOauth2Client.getUserInfo(authCode);
		Member member = getMember(hiworksUserInfoResponseDto);
		String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail(), member.getAuthorities());

		return OauthLoginResponseDto.builder()
			.accessToken(accessToken)
			.build();
	}

	private Member getMember(HiworksUserInfoResponseDto hiworksUserInfoResponseDto) {
		Member member = memberRepository.findByEmail(hiworksUserInfoResponseDto.hiworksEmail())
			.orElse(null);
		if (member == null) {
			return saveMember(hiworksUserInfoResponseDto);
		}
		return member;
	}

	private Member saveMember(HiworksUserInfoResponseDto hiworksUserInfoResponseDto) {
		Member member = Member.builder()
			.email(hiworksUserInfoResponseDto.hiworksEmail())
			.name(hiworksUserInfoResponseDto.userName())
			.roles(RoleType.MEMBER.name())
			.build();
		return memberRepository.save(member);
	}

}
