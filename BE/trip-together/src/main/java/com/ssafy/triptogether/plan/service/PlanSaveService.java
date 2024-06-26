package com.ssafy.triptogether.plan.service;

import com.ssafy.triptogether.plan.data.request.PlansSaveRequest;

public interface PlanSaveService {
    long plansSave(Long memberId, PlansSaveRequest plansSaveRequest);

    void planDelete(Long memberId, Long planId);

    void planModify(Long memberId, Long planId, PlansSaveRequest plansSaveRequest);
}