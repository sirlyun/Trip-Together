package com.ssafy.twinklebank.account.controller;

import com.ssafy.twinklebank.account.data.request.AccountDeleteRequest;
import com.ssafy.twinklebank.account.data.request.Verify1wonRequest;
import com.ssafy.twinklebank.account.data.response.AccountResponse;
import com.ssafy.twinklebank.account.data.request.AddAccountRequest;
import com.ssafy.twinklebank.account.data.request.DepositWithdrawRequest;
import com.ssafy.twinklebank.account.data.request.Transfer1wonRequest;
import com.ssafy.twinklebank.account.service.AccountLoadService;
import com.ssafy.twinklebank.account.service.AccountSaveService;
import com.ssafy.twinklebank.auth.utils.SecurityMember;
import com.ssafy.twinklebank.global.data.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssafy.twinklebank.global.data.response.StatusCode.*;
import static org.springframework.http.HttpStatus.*;

@RequestMapping("account/v1/accounts")
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountSaveService accountSaveService;
    private final AccountLoadService accountLoadService;

    @GetMapping("{account_id}/balance")
    public ResponseEntity<ApiResponse<Double>> getBalance(
        @AuthenticationPrincipal SecurityMember securityMember,
        @RequestParam("account_id") String accountId
    ) {
        long memberId = securityMember.getId();
        double balance = accountLoadService.getBalance(memberId, accountId);
        return ApiResponse.toResponseEntity(OK, SUCCESS_GET_BALANCE, balance);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getUserAccountList(
        @AuthenticationPrincipal SecurityMember securityMember,
        @RequestParam("client_id") String clientId
    ) {
        long userId = securityMember.getId();
        List<AccountResponse> accountResponseList = accountLoadService.getAccounts(clientId, userId);

        return ApiResponse.toResponseEntity(OK, SUCCESS_GET_ACCOUNT_LIST, accountResponseList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addLinkedAccount(
        @RequestBody @Valid AddAccountRequest addAccountRequest,
        @RequestParam("client_id") String clientId
    ) {
        accountSaveService.addLinkedAccount(clientId, addAccountRequest);
        return ApiResponse.emptyResponse(CREATED, CREATED_LINKED_ACCOUNT);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteLinkedAccount(
            @AuthenticationPrincipal SecurityMember securityMember,
            @RequestParam("client_id") String clientId,
            @Valid @RequestBody AccountDeleteRequest accountDeleteRequest
    ) {
        Long memberId = securityMember.getId();
        accountSaveService.deleteLinkedAccount(clientId, memberId, accountDeleteRequest);
        return ApiResponse.emptyResponse(NO_CONTENT, DELETE_LINKED_ACCOUNT);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Void>> deposit(
            @AuthenticationPrincipal SecurityMember securityMember,
            @Valid @RequestBody DepositWithdrawRequest depositRequest
    ) {
        Long memberId = securityMember.getId();
        accountSaveService.deposit(memberId, depositRequest);
        return ApiResponse.emptyResponse(OK, SUCCESS_DEPOSIT);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
        @AuthenticationPrincipal SecurityMember securityMember,
        @Valid @RequestBody DepositWithdrawRequest withdrawRequest
    ) {
        Long memberId = securityMember.getId();
        accountSaveService.withdraw(memberId, withdrawRequest);
        return ApiResponse.emptyResponse(OK, SUCCESS_WITHDRAW);
    }

    @PostMapping("/1wontransfer")
    public ResponseEntity<ApiResponse<Void>> transfer1won(
        @AuthenticationPrincipal SecurityMember securityMember,
        @Valid @RequestBody Transfer1wonRequest request){
        Long memberId = securityMember.getId();
        accountSaveService.transfer1won(memberId, request);
        return ApiResponse.emptyResponse(OK, SUCCESS_1WON_TRANSFER);
    }

    @PostMapping("/1wonverify")
    public ResponseEntity<ApiResponse<Void>> verify1won(
        @AuthenticationPrincipal SecurityMember securityMember, @Valid @RequestBody Verify1wonRequest request){
        accountSaveService.verify1won(request);
        return ApiResponse.emptyResponse(OK, SUCCESS_VERIFY_1WON);
    }
}
