package com.ssafy.twinklebank.member.service;

import com.ssafy.twinklebank.member.data.AuthInfoFindResponse;

public interface MemberLoadService {
    AuthInfoFindResponse findAuthInfo(String memberUuid);
}
