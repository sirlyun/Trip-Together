package com.ssafy.triptogether.syncaccount.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsDetail;
import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsLoadResponse;
import com.ssafy.triptogether.syncaccount.domain.SyncAccount;
import com.ssafy.triptogether.syncaccount.repository.SyncAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SyncAccountServiceImpl implements SyncAccountLoadService {
	// Repository
	private final SyncAccountRepository syncAccountRepository;

	/**
	 * 사용자의 연동 계좌 목록 조회
	 * @param memberId 요청자의 member_id
	 * @return 연동 계좌 목록
	 */
	@Override
	public SyncAccountsLoadResponse syncAccountsLoad(Long memberId) {
		List<SyncAccountsDetail> syncAccounts = syncAccountRepository.memberSyncAccountsLoad(memberId);
		return SyncAccountsLoadResponse.builder()
			.syncAccountsDetail(syncAccounts)
			.build();
	}
}