package com.runninghi.image.command.application.dto.response;

import com.runninghi.user.command.domain.aggregate.entity.enumtype.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record ImageUserResponse(

        UUID id,
        String account,
        String name,
        String nickname,
        String email,
        String kakaoId,
        String KakaoName,
        int reportCount,
        boolean blacklistStatus,
        boolean status,
        Role role,
        LocalDateTime createdAt

) { }
