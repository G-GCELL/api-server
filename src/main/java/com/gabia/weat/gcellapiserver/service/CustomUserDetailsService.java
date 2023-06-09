package com.gabia.weat.gcellapiserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws CustomException {
		return memberRepository.findByEmail(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

}