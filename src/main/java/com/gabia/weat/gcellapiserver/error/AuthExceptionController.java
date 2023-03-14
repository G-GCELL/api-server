package com.gabia.weat.gcellapiserver.error;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.error.exception.CustomException;

@RestController
@RequestMapping("/auth/exception")
public class AuthExceptionController {

	@GetMapping("/entry")
	public void throwAuthEntryPointException() {
		throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
	}

	@GetMapping("/denied")
	public void throwAccessDeniedException() {
		throw new CustomException(ErrorCode.ACCESS_FORBIDDEN);
	}

}
