package com.ssafy.triptogether.infra.twinklebank;

import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.exception.exceptions.category.ExternalServerException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleAccountSyncRequest;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankAccountsLoadRequest;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankLogoutRequest;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleAccountSyncResponse;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleBankAccountsLoadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class TwinkleBankClientImpl implements TwinkleBankClient {
	private final RestTemplate restTemplate;
	static final String TWINKLE_BANK_URI = "https://www.twinklebank.go.kr";

	/**
	 * 요청자의 은행 계좌 목록 조회 요청
	 * @param twinkleBankAccountsLoadRequest 요청자 정보
	 * @return 은행 계좌 목록
	 */
	@Override
	public TwinkleBankAccountsLoadResponse bankAccountsLoad(
		TwinkleBankAccountsLoadRequest twinkleBankAccountsLoadRequest) {
		// Todo: twinkleBankAccountsLoadRequest 에서 uuid 를 뽑아서 Redis 에서 access_token 조회

		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/account/v1/accounts")
			.queryParam("여행 클라이언트 키")
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + "access_token");
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<ApiResponse> response = restTemplate.exchange(
			url,
			HttpMethod.GET,
			entity,
			ApiResponse.class
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			return (TwinkleBankAccountsLoadResponse)response.getBody().getData();
		}

		throw new ExternalServerException("TwinkleBankAccountsLoad", ErrorCode.TWINKLE_BANK_SERVER_ERROR);
	}

	/**
	 * 계좌 연동 요청
	 *
	 * @param twinkleAccountSyncRequest 연동하고자 하는 계좌
	 * @return 연동된 계좌의 이름과 계좌 번호
	 */
	@Override
	public TwinkleAccountSyncResponse bankAccountsSync(TwinkleAccountSyncRequest twinkleAccountSyncRequest) {
		// Todo: twinkleBankAccountsLoadRequest 에서 uuid 를 뽑아서 Redis 에서 access_token 조회

		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/account/v1/accounts")
			.queryParam("여행 클라이언트 키")
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + "access_token");
		HttpEntity<TwinkleAccountSyncRequest> entity = new HttpEntity<>(twinkleAccountSyncRequest, headers);

		ResponseEntity<ApiResponse> response = restTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			ApiResponse.class
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			return (TwinkleAccountSyncResponse)response.getBody().getData();
		}

		throw new ExternalServerException("TwinkleBankAccountsLoad", ErrorCode.TWINKLE_BANK_SERVER_ERROR);
	}

	/**
	 * 계좌 연동 해지 요청
	 * @param twinkleAccountSyncRequest 연동 해지 할 계좌
	 */
	@Override
	public void bankAccountSyncDelete(TwinkleAccountSyncRequest twinkleAccountSyncRequest) {
		// Todo: twinkleBankAccountsLoadRequest 에서 uuid 를 뽑아서 Redis 에서 access_token 조회

		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/account/v1/accounts")
			.queryParam("여행 클라이언트 키")
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + "access_token");
		HttpEntity<TwinkleAccountSyncRequest> entity = new HttpEntity<>(twinkleAccountSyncRequest, headers);

		ResponseEntity<ApiResponse> response = restTemplate.exchange(
			url,
			HttpMethod.DELETE,
			entity,
			ApiResponse.class
		);

		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ExternalServerException("TwinkleBankAccountsLoad", ErrorCode.TWINKLE_BANK_SERVER_ERROR);
		}
	}

	@Override
	public void bankLogout(TwinkleBankLogoutRequest twinkleBankLogoutRequest) {
		// Todo: twinkleBankAccountsLoadRequest 에서 uuid 를 뽑아서 Redis 에서 access_token 조회 & 해당 저장 삭제

		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/member/v1/members/logout")
				.queryParam("여행 클라이언트 키")
				.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + "access_token");
		HttpEntity<TwinkleBankLogoutRequest> entity = new HttpEntity<>(twinkleBankLogoutRequest, headers);

		ResponseEntity<ApiResponse> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				entity,
				ApiResponse.class
		);

		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ExternalServerException("TwinkleBankAccountsLoad", ErrorCode.TWINKLE_BANK_SERVER_ERROR);
		}
	}
}
