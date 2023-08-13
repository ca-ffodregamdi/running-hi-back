package com.runninghi.User.command.application.service;

import com.runninghi.user.command.application.dto.sign_in.request.SignInRequest;
import com.runninghi.user.command.application.dto.sign_in.response.SignInResponse;
import com.runninghi.user.command.application.dto.sign_up.request.SignUpRequest;
import com.runninghi.user.command.application.dto.sign_up.response.SignUpResponse;
import com.runninghi.user.command.application.service.SignService;
import com.runninghi.user.command.domain.aggregate.entity.User;
import com.runninghi.user.command.domain.aggregate.entity.enumtype.Role;
import com.runninghi.user.command.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SignServiceTest {
    private final SignService signService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    SignServiceTest(SignService signService, UserRepository userRepository, PasswordEncoder encoder) {
        this.signService = signService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @BeforeEach
    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입() {
        // given
        SignUpRequest request = new SignUpRequest("qwerty1234", "1234", "qweqwe");
        // when
        SignUpResponse response = signService.registUser(request);
        // then
        Assertions.assertThat(response.account()).isEqualTo("qwerty1234");
        Assertions.assertThat(response.name()).isEqualTo("qweqwe");
    }

    @Test
    void 아이디는_중복될_수_없다() {
        // given
        userRepository.save(User.builder()
                .account("qwerty1234")
                .password(encoder.encode("1234"))
                .name("qweqwe")
                .role(Role.USER)
                .build());
        SignUpRequest request = new SignUpRequest("qwerty1234", "1234", null);
        // when
        // then
        Assertions.assertThatThrownBy(() -> signService.registUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 아이디입니다.");
    }

    @Test
    void 로그인() {
        // given
        userRepository.save(User.builder()
                .account("qwerty1234")
                .password(encoder.encode("1234"))
                .name("qweqwe")
                .role(Role.USER)
                .build());
        // when
        SignInResponse response = signService.signIn(new SignInRequest("qwerty1234", "1234"));
        // then
        Assertions.assertThat(response.name()).isEqualTo("qweqwe");
        Assertions.assertThat(response.role()).isEqualTo(Role.USER);
    }

    @Test
    void 로그인실패() {
        // given
        userRepository.save(User.builder()
                .account("qwerty1234")
                .password(encoder.encode("1234"))
                .name("qweqwe")
                .role(Role.USER)
                .build());
        // when
        // then
        Assertions.assertThatThrownBy(() -> signService.signIn(new SignInRequest("qwerty1234", "12345")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}