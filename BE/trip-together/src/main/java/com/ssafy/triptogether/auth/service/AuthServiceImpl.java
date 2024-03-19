package com.ssafy.triptogether.auth.service;

import org.springframework.stereotype.Service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthLoadService {
	private final MemberRepository memberRepository;

	/**
	 * 사용자 PIN 인증을 진행하는 메서드
	 * @param memberId 인증된 사용자의 member_id
	 * @param pinVerifyRequest 요청 PIN 번호
	 * @return 성공 시 null 반환
	 */
	@Override
	public Void pinVerify(Long memberId, PinVerifyRequest pinVerifyRequest) {
		// Todo: 해당 member 가 존재하지 않는 예외 처리 작성
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new NotFoundException("PinVerify", ErrorCode.UNDEFINED_MEMBER, memberId)
			);
		// Todo: inputPin 을 암호화한 후 member 의 PIN 과 비교

		// Todo: 일치하지 않는다면 예외 처리

		return null;
	}
}
