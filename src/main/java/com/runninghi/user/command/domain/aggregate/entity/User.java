package com.runninghi.user.command.domain.aggregate.entity;

import com.runninghi.common.entity.BaseEntity;
import com.runninghi.user.command.application.dto.request.UserUpdateRequest;
import com.runninghi.user.command.application.dto.sign_up.request.SignUpRequest;
import com.runninghi.user.command.domain.aggregate.entity.enumtype.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
@Table(name = "TBL_USER")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String account; // 폼 로그인 아이디
    private String password; // 폼 로그인 비밀번호
    private String name; // 폼 로그인 이름
    private String location; // 추가로 현 위치 받을 때
    private String nickname; // 폼 로그인 닉네임
    private String kakaoId; // 카카오 고유 아이디
    private String email; // 카카오 계정에 등록된 이메일
    private String kakaoName; // 카카오 닉네임
    private int reportCount; // 신고 횟수
    private boolean blacklistStatus; // 블랙리스트 상태
    private boolean status; // 회원 상태 (true = 회원, false = 삭제)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String provider; // 어떤 OAuth인지 (google, naver 등)
    private String provideId; // 해당 OAuth 의 key(id)

    @Builder
    private User(String account, String password, String name, Integer age, Role role) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static User from(SignUpRequest request, PasswordEncoder encoder) {
        return User.builder()
                .account(request.account())
                .password(encoder.encode(request.password()))
                .name(request.name())
                .role(Role.USER)
                .build();
    }

    public void update(UserUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());
        this.name = newMember.name();
    }
}
