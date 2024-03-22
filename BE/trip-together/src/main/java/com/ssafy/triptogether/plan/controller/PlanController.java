package com.ssafy.triptogether.plan.controller;

import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.data.response.StatusCode;
import com.ssafy.triptogether.plan.data.request.PlansSaveRequest;
import com.ssafy.triptogether.plan.data.response.PlanDetailFindResponse;
import com.ssafy.triptogether.plan.service.PlanLoadService;
import com.ssafy.triptogether.plan.service.PlanSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.triptogether.global.data.response.StatusCode.SUCCESS_PLAN_DETAIL_FIND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/plan/v1/plans")
@RequiredArgsConstructor
public class PlanController {
	// Service
	private final PlanSaveService planSaveService;
	private final PlanLoadService planLoadService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> plansSave(
		@RequestBody @Valid PlansSaveRequest plansSaveRequest
	) {
		planSaveService.plansSave(1L, plansSaveRequest);

		return ApiResponse.emptyResponse(
			HttpStatus.CREATED, StatusCode.SUCCESS_PLANS_SAVE
		);
	}

	@DeleteMapping("/{plan_id}")
	public ResponseEntity<ApiResponse<Void>> planDelete(
		@PathVariable("plan_id") Long planId
	) {
		planSaveService.planDelete(1L, planId);

		return ApiResponse.emptyResponse(
			HttpStatus.NO_CONTENT, StatusCode.SUCCESS_PLAN_DELETE
		);
	}

	@GetMapping("/{plan_id}")
	public ResponseEntity<ApiResponse<PlanDetailFindResponse>> findPlanDetail(
			@PathVariable("plan_id") long planId
	) {
		PlanDetailFindResponse response = planLoadService.findPlanDetail(planId);
		return ApiResponse.toResponseEntity(OK, SUCCESS_PLAN_DETAIL_FIND, response);
	}
}
