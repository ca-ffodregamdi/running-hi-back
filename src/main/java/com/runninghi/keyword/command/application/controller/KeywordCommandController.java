package com.runninghi.keyword.command.application.controller;

import com.runninghi.common.response.ApiResponse;
import com.runninghi.keyword.command.application.dto.request.KeywordCreateRequest;
import com.runninghi.keyword.command.application.dto.response.KeywordResponse;
import com.runninghi.keyword.command.application.service.KeywordCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "키워드 커멘드 컨트롤러")
@RestController
@RequiredArgsConstructor
public class KeywordCommandController {

    private final KeywordCommandService keywordCommandService;

    @Operation(summary = "키워드 생성")
    @PostMapping("api/v1/keyword")
    public ResponseEntity<ApiResponse> createKeyword(KeywordCreateRequest request) {
        Optional.ofNullable(request.userKey())
                .orElseThrow( () -> new NullPointerException("로그인 후 이용해주세요."));
        keywordCommandService.checkAdminByUserKey(request.userKey());  // 관리자 아닐 시 예외
        KeywordResponse keyword = keywordCommandService.createKeyword(request.keywordName());

        return ResponseEntity.ok(ApiResponse.success("성공적으로 등록되었습니다.", keyword));
    }

}
