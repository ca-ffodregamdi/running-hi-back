package com.runninghi.user.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TBL_USER_REFRESH_TOKEN")
public class UserRefreshToken {
    @Id
    private UUID userId;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    private String refreshToken;
    private int reissueCount = 0;

    public UserRefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void increaseReissueCount() {
        reissueCount++;
    }
}