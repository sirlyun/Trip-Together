package com.ssafy.twinklebank.auth.controller;

import static com.ssafy.twinklebank.global.data.response.StatusCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.twinklebank.auth.data.request.TokenRequest;
import com.ssafy.twinklebank.auth.data.response.TokenResponse;
import com.ssafy.twinklebank.auth.provider.CookieProvider;
import com.ssafy.twinklebank.auth.service.AuthServiceImpl;
import com.ssafy.twinklebank.global.data.response.ApiResponse;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member/v1/oauth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthServiceImpl authService;
	private final CookieProvider cookieProvider;

	@PostMapping("/token")
	public ResponseEntity<ApiResponse<TokenResponse>> getToken(@RequestBody TokenRequest request) {

		Map<String, String> tokenMap = authService.getToken(request);
		String accessToken = tokenMap.get("access");
		String refreshToken = tokenMap.get("refresh");

		// refresh token은 헤더에 쿠키에 넣어준다
		ResponseCookie cookie = cookieProvider.createCookie(refreshToken);

		// 쿠키를 담을 헤더 생성
		HttpHeaders headers = cookieProvider.addCookieHttpHeaders(cookie);

		// ApiResponse 객체 생성
		ApiResponse<TokenResponse> apiResponse = getApiResponse(accessToken);

		// ResponseEntity에 헤더와 함께 ApiResponse 객체를 담아 반환
		return ResponseEntity
			.status(HttpStatus.OK)
			.headers(headers)
			.body(apiResponse);
	}

	private static ApiResponse<TokenResponse> getApiResponse(String accessToken) {
		return ApiResponse.<TokenResponse>builder()
			.status(SUCCESS_GENERATE_TOKEN.getStatus())
			.message(SUCCESS_GENERATE_TOKEN.getMessage())
			.data(new TokenResponse(accessToken))
			.build();
	}

}
