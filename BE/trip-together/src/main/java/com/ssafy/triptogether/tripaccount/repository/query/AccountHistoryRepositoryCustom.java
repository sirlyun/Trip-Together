package com.ssafy.triptogether.tripaccount.repository.query;

import com.ssafy.triptogether.tripaccount.domain.AccountHistory;
import com.ssafy.triptogether.tripaccount.domain.CurrencyCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountHistoryRepositoryCustom {
    Page<AccountHistory> findAccountHistoriesLoadDetailByMemberId(Long memberId, Pageable pageable, CurrencyCode currencyCode);
}
