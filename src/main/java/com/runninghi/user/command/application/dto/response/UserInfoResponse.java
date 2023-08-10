package com.runninghi.user.command.application.dto.response;

import com.runninghi.user.command.domain.aggregate.entity.User;
import com.runninghi.user.command.domain.aggregate.entity.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserInfoResponse(
        @Schema(description = "회원 고유키", example = "c0a80121-7aeb-4b4b-8b0a-6b1c032f0e4a")
        UUID id,
        @Schema(description = "회원 아이디", example = "colabear754")
        String account,
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 타입", example = "USER")
        Role role,
        @Schema(description = "회원 생성일", example = "2023-05-11T15:00:00")
        LocalDateTime createdAt
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getAccount(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}