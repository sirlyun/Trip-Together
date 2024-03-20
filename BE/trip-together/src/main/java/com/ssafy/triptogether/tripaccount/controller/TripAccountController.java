package com.ssafy.triptogether.tripaccount.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.data.response.StatusCode;
import com.ssafy.triptogether.tripaccount.data.response.CurrenciesLoadResponse;
import com.ssafy.triptogether.tripaccount.service.TripAccountLoadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account/v1/trip-account")
@RequiredArgsConstructor
public class TripAccountController {
	// Service
	private final TripAccountLoadService tripAccountLoadService;

	@GetMapping("/currencies")
	public ResponseEntity<ApiResponse<CurrenciesLoadResponse>> currenciesLoad() {
		CurrenciesLoadResponse currenciesLoadResponse = tripAccountLoadService.currenciesLoad();

		return ApiResponse.toResponseEntity(
			HttpStatus.OK, StatusCode.SUCCESS_CURRENCY_LOAD, currenciesLoadResponse
		);
	}
}