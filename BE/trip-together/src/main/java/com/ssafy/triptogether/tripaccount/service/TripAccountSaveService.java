package com.ssafy.triptogether.tripaccount.service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.tripaccount.data.request.AccountHistorySaveRequest;
import com.ssafy.triptogether.tripaccount.data.request.TripAccountExchangeRequest;
import com.ssafy.triptogether.tripaccount.data.request.TripAccountPaymentRequest;

public interface TripAccountSaveService {
	void currencyRateUpdate();

	void tripAccountExchange(long memberId, PinVerifyRequest pinVerifyRequest,
		TripAccountExchangeRequest tripAccountExchangeRequest);

	void tripAccountPay(long memberId, PinVerifyRequest pinVerifyRequest,
		TripAccountPaymentRequest tripAccountPaymentRequest);
}
