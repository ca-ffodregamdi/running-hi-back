package com.runninghi.adminpost.command.application.service;

import com.runninghi.adminpost.command.application.controller.AdminPostCommandController;
import com.runninghi.adminpost.command.application.dto.request.AdminPostRequest;
import com.runninghi.adminpost.command.application.dto.request.KeywordListRequest;
import com.runninghi.adminpost.command.domain.repository.AdminPostCommandRepository;
import com.runninghi.keyword.command.domain.repository.KeywordCommandRepository;
import com.runninghi.user.command.domain.aggregate.entity.User;
import com.runninghi.user.command.domain.aggregate.entity.enumtype.Role;
import com.runninghi.user.command.domain.repository.UserCommandRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class AdminPostCommandServiceTest {

    @Autowired
    private AdminPostCommandController adminPostCommandController;
    @Autowired
    private UserCommandRepository userCommandRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AdminPostCommandService adminPostCommandService;
    @Autowired
    private AdminPostCommandRepository adminPostCommandRepository;
    @Autowired
    private KeywordCommandRepository keywordCommandRepository;


    @BeforeEach
    @AfterEach
    void clear() {
        userCommandRepository.deleteAllInBatch();
        adminPostCommandRepository.deleteAllInBatch();
        keywordCommandRepository.deleteAllInBatch();}

    private User createAdmin() {
        return userCommandRepository.save(User.builder()
                .account("qwerty1234")
                .password(encoder.encode("1234"))
                .name("김철수")
                .nickname("qwe")
                .email("qwe@qwe.qw")
                .role(Role.ADMIN)
                .status(true)
                .build());
    }

    private AdminPostRequest createAdminPostRequest(User admin, List<KeywordListRequest> keywordList) {
        return new AdminPostRequest(
                admin.getId(),
                "asdfiasdnfo.jpg",
                "테스트 제목",
                "테스트 내용",
                keywordList
        );

    }

    private List<KeywordListRequest> createKeywordList() {
        return Arrays.asList(
                KeywordListRequest.of(1L, "낮"),
                KeywordListRequest.of(2L, "밤")
        );
    }

    @DisplayName("관리자 게시글 생성 테스트 : success")
    @Test
    void testCreateAdminPostSuccess() {

        // given
        User admin = createAdmin();
        List<KeywordListRequest> keywordList = createKeywordList();
        AdminPostRequest request = createAdminPostRequest(admin, keywordList);


        // when & then
        Assertions.assertThatCode(
                () -> adminPostCommandService.createAdminPost(request)
        ).doesNotThrowAnyException();
    }

    @DisplayName("관리자 게시글 생성 테스트 : 키워드 부분이 null 값이 포함되어 있을 때")
    @Test
    void testCreateAdminPostNPE() {

        // given
        User admin = createAdmin();
        AdminPostRequest request = new AdminPostRequest(
                admin.getId(),
                "asdfiasdnfo.jpg",
                "테스트 제목",
                "테스트 내용",
                null
        );

        Assertions.assertThatThrownBy(
                () -> adminPostCommandService.createAdminPost(request)
        ).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("관리자 게시글 수정 테스트 : success")
    @Test
    void testupdateAdminPostSuccess() {

        // giver
    }
}