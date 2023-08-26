package com.runninghi.adminpost.command.application.controller;

import com.runninghi.adminpost.command.application.dto.request.AdminPostCreateRequest;
import com.runninghi.adminpost.command.application.dto.response.AdminPostResponse;
import com.runninghi.adminpost.command.application.service.AdminPostCommandService;
import com.runninghi.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "관리자 게시글 Command")
@RestController
@RequiredArgsConstructor
public class AdminPostCommandController {

    private final AdminPostCommandService adminPostCommandService;

    @Operation(summary = "관리자 게시글 생성")
    @PostMapping("api/v1/admin-post")
    public ResponseEntity<ApiResponse> createAdminPost(@RequestBody AdminPostCreateRequest request) {
        Optional.ofNullable(request.userKey())
                .orElseThrow( () -> new NullPointerException("로그인 후 이용해주세요."));
        adminPostCommandService.checkAdminByUserNo(request.userKey());
        AdminPostResponse response = adminPostCommandService.createAdminPost(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("성공적으로 저장되었습니다.", response)
        );
    }
}
