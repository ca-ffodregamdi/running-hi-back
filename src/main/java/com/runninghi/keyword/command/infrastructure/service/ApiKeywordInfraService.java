package com.runninghi.keyword.command.infrastructure.service;

import com.runninghi.common.annotation.InfraService;
import com.runninghi.keyword.command.application.dto.response.KeywordResponse;
import com.runninghi.keyword.command.application.dto.response.UserCheckResponse;
import com.runninghi.keyword.command.domain.service.ApiKeywordDomainService;
import com.runninghi.keyword.query.application.dto.response.FindKeywordResponse;
import com.runninghi.keyword.query.application.service.KeywordQueryService;
import com.runninghi.user.query.application.dto.user.response.UserInfoResponse;
import com.runninghi.user.query.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@InfraService
@RequiredArgsConstructor
public class ApiKeywordInfraService implements ApiKeywordDomainService {

    private final KeywordQueryService keywordQueryService;
    private final UserQueryService userQueryService;

    @Override
    public UserCheckResponse checkUserByUserKey(UUID userKey) {
        UserInfoResponse result = userQueryService.findUserInfo(userKey);
        return new UserCheckResponse(
                result.id(),
                result.account(),
                result.name(),
                result.role(),
                result.createdAt()
        );
    }

    @Override
    public KeywordResponse findByKeywordNo(Long keywordNo) {
        FindKeywordResponse response =
                keywordQueryService.findKeywordByKeywordNo(keywordNo);
        return KeywordResponse.of(response.keywordNo(), response.keywordName());
    }
}
