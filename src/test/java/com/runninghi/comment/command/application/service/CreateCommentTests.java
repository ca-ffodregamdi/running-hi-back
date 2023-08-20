package com.runninghi.comment.command.application.service;

import com.runninghi.bookmarkfolder.command.application.dto.request.CreateFolderRequest;
import com.runninghi.bookmarkfolder.command.application.service.CreateNewBookmarkFolderService;
import com.runninghi.bookmarkfolder.command.domain.repository.BookmarkFolderRepository;
import com.runninghi.comment.command.application.dto.request.CreateCommentRequest;
import com.runninghi.comment.command.domain.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;


@SpringBootTest
@Transactional
public class CreateCommentTests {

    @Autowired
    private CreateCommentService createCommentService;

    @Autowired
    private CommentRepository commentRepository;


    @Test
    @DisplayName("생성: 댓글 추가 기능 테스트")
    void testCreateComment() {

        long beforeSize = commentRepository.count();

        CreateCommentRequest commentRequest = new CreateCommentRequest(UUID.randomUUID(), 1L, "댓글 생성 테스트");
        createCommentService.createComment(commentRequest);

        long afterSize = commentRepository.count();

        org.junit.jupiter.api.Assertions.assertEquals(beforeSize + 1, afterSize);
    }

    @Test
    @DisplayName("생성: 댓글 내용 공백일 때 예외처리")
    void testCommentIsBlank() {

        CreateCommentRequest commentRequest = new CreateCommentRequest(UUID.randomUUID(), 1L, "         ");
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> createCommentService.createComment(commentRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글은 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("생성: 댓글 내용 null일 때 예외처리")
    void testCommentIsNull() {

        CreateCommentRequest commentRequest = new CreateCommentRequest(UUID.randomUUID(), 1L, null);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> createCommentService.createComment(commentRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글은 공백일 수 없습니다.");
    }
}
