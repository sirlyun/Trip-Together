package com.ssafy.twinklebank.global.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
	// MEMBER
	SUCCESS_AUTH_INFO_FIND(200, "은행 사용자 정보 조회 성공"),
	SUCCESS_JOIN(200, "회원가입 성공"),
	SUCCESS_GENERATE_TOKEN(200, "토큰 발급 성공"),
	SUCCESS_GET_ACCOUNT_LIST(200, "계좌 목록 조회 성공"),
	SUCCESS_LOGOUT(200, "로그아웃 성공"),
	SUCCESS_REISSUE(200, "토큰 재발급 성공"),
	// ACCOUNT
	CREATED_LINKED_ACCOUNT(201, "계좌 연동 성공"),
	DELETE_LINKED_ACCOUNT(204, "계좌 해지 성공"),
	SUCCESS_DEPOSIT(200, "입금 성공"),
	SUCCESS_WITHDRAW(200, "출금 성공"),
	SUCCESS_GET_BALANCE(200, "계좌 목록 조회 성공"),

	SUCCESS_1WON_TRANSFER(200, "1원 전송 성공"),

	SUCCESS_CREATE_CODE(201, "CODE 발급성공"),
	SUCCESS_VERIFY_1WON(200, "1원 인증 성공")
	;
	//
	// CREATED_USER(201, "회원가입 성공"),
	//
	// SUCCESS_LOGOUT(200, "로그아웃 성공"),
	//
	// EMAIL_IN_USE(200, "사용중인 이메일입니다."),
	//
	// EMAIL_NOT_IN_USE(200, "사용중이지 않은 이메일입니다."),
	//
	// EMAIL_CERT_SUCCESS(200, "이메일 인증이 완료되었습니다."),
	//
	// NICKNAME_IN_USE(200, "사용중인 닉네임입니다."),
	//
	// NICKNAME_NOT_IN_USE(200, "사용중이지 않은 닉네임입니다."),
	//
	// SUCCESS_RESIGNATION(200, "회원 탈퇴 성공"),
	//
	// SUCCESS_PASSWORD_SET(200, "비밀번호 수정 완료"),
	//
	// KAKAO_JOIN_NEEDED(203, "kakao 회원이 아닙니다. 회원가입 진행 필요")
	// ;

	private final int status;
	private final String message;
}