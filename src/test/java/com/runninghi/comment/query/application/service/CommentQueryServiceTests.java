package com.runninghi.comment.query.application.service;

import com.runninghi.comment.command.application.dto.request.CreateCommentRequest;
import com.runninghi.comment.command.application.service.CommentCommandService;
import com.runninghi.comment.command.domain.aggregate.entity.Comment;
import com.runninghi.comment.command.domain.repository.CommentRepository;
import com.runninghi.comment.query.application.dto.request.FindAllCommentsRequest;
import com.runninghi.comment.query.application.dto.request.FindCommentRequest;
import com.runninghi.feedback.command.domain.exception.customException.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@SpringBootTest
@Transactional
public class CommentQueryServiceTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentQueryService queryCommentService;

    @Autowired
    private CommentCommandService createCommentService;

    @Test
    @DisplayName("댓글 전체 조회 테스트 : success")
    void testFindCommentsByPostNo() {

        Long userPostNo = 999L;

        commentRepository.save(Comment.builder()
                .userPostNo(userPostNo)
                .build());

        commentRepository.save(Comment.builder()
                .userPostNo(userPostNo)
                .build());

        Pageable pageable = PageRequest.of(0, 10);      //추후 수정 필요
        Page<Comment> commentsPage = queryCommentService.findAllComments(new FindAllCommentsRequest(userPostNo), pageable);

        Assertions.assertEquals(2, commentsPage.getTotalElements());

    }

    @Test
    @DisplayName("특정 댓글 조회 테스트 : success")
    void testFindCommentByCommentNo() {

        CreateCommentRequest commentRequest = new CreateCommentRequest(UUID.randomUUID(), 1L, "댓글 생성 테스트");
        Comment comment = createCommentService.createComment(commentRequest);

        Assertions.assertEquals(comment, queryCommentService.findComment(new FindCommentRequest(comment.getCommentNo())));

    }

    @Test
    @DisplayName("댓글 조회 테스트: 댓글 없을 시 예외처리")
    void testCommentNoDoesntExist() {

        FindCommentRequest commentRequest = new FindCommentRequest(0L);

        Assertions.assertThrows(NotFoundException.class, () -> {
            queryCommentService.findComment(commentRequest);
        });

    }

    @Test
    @DisplayName("댓글 조회 테스트: 게시글 없을 시 예외처리")
    void testPostNoDoesntExist() {

        //존재하지 않는 게시물일시 NotFoundException 처리

    }

}