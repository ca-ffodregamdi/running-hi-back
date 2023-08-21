package com.runninghi.postreport.command.application.service;

import com.runninghi.feedback.command.domain.exception.customException.NotFoundException;
import com.runninghi.postreport.command.application.dto.request.PostReportRequest;
import com.runninghi.postreport.command.domain.aggregate.entity.PostReport;
import com.runninghi.postreport.command.domain.aggregate.vo.PostReportUserVO;
import com.runninghi.postreport.command.domain.aggregate.vo.PostReportedUserVO;
import com.runninghi.postreport.command.domain.aggregate.vo.ReportedPostVO;
import com.runninghi.postreport.command.domain.repository.PostReportCommandRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostReportCommandServiceTests {

    @Autowired
    private PostReportCommandRepository postReportCommandRepository;

    @Autowired
    private PostReportCommandService postReportCommandService;

    @Test
    @DisplayName("게시글 신고 저장 테스트: DTO 엔티티 변환 후 저장 확인")
    void savePostReportTest() {

        //given
        Long before = postReportCommandRepository.count();

        UUID reportUserNo = UUID.randomUUID();
        UUID reportedUserNo = UUID.randomUUID();
        Long reportedPostNo = 1L;

        //when
        PostReportRequest postReportRequest = new PostReportRequest(1, "욕설");

        PostReport postReport = PostReport.builder()
                .postReportCategoryCode(postReportRequest.postReportCategoryCode())
                .postReportContent(postReportRequest.postReportContent())
                .postReportedDate(LocalDateTime.now())
                .postReportUserVO(new PostReportUserVO(reportUserNo))
                .postReportedUserVO(new PostReportedUserVO(reportedUserNo))
                .reportedPostVO(new ReportedPostVO(reportedPostNo))
                .build();

        postReportCommandRepository.save(postReport);
        Long after = postReportCommandRepository.count();

       //then
        Assertions.assertEquals(1, after - before);
    }

    @Test
    @DisplayName("게시글 신고 저장 테스트: 카테고리 미선택 시 저장 안하는지 확인")
    void checkPostReportCategoryCodeTest() {

        //given
        Long before = postReportCommandRepository.count();

        PostReportRequest postReportRequest =
                new PostReportRequest(0, "욕설");

        //when
        // 필기. 예외의 유형, () -> 테스트할 코드
        Assertions.assertThrows(IllegalArgumentException.class, () -> postReportCommandService.savePostReport(postReportRequest));

        Long after = postReportCommandRepository.count();

        //then
        Assertions.assertEquals(after, before);
    }

    @Test
    @DisplayName("게시글 신고 저장 테스트: 신고 내용 공백일 시 저장 안하는지 확인")
    void checkPostReportContentTest() {

        //given
        Long before = postReportCommandRepository.count();

        PostReportRequest postReportRequest =
                new PostReportRequest(1, "");

        //when
        Assertions.assertThrows(IllegalArgumentException.class, () -> postReportCommandService.savePostReport(postReportRequest));

        Long after = postReportCommandRepository.count();

        //then
        Assertions.assertEquals(after, before);
    }

    @Test
    @DisplayName("게시글 신고 저장 테스트: 신고 내용 100자 초과일 시 저장 안하는지 확인")
    void checkPostReportContentLengthTest() {

        //given
        Long before = postReportCommandRepository.count();

        String str = "a".repeat(101);

        PostReportRequest postReportRequest =
                new PostReportRequest(1, str);

        //when
        assertThatThrownBy(() -> postReportCommandService.savePostReport(postReportRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("신고 내용은 100자를 넘을 수 없습니다.");

        Long after = postReportCommandRepository.count();

        //then
        Assertions.assertEquals(after, before);
    }
    @Test
    @DisplayName("게시글 신고 조회 테스트: 상세조회 성공")
    void findPostReportTest() {

        //given
        PostReportRequest postReportRequest = new PostReportRequest(2, "홍보 게시물");

        //when
        PostReport savedPostReport = postReportCommandService.savePostReport(postReportRequest);

        PostReport findedPostReport = postReportCommandRepository.findById(savedPostReport.getPostReportNo()).get();

        //then
        assertThat(findedPostReport.getPostReportContent().equals("홍보 게시물"));
        assertThat(findedPostReport.getPostReportCategoryCode()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 신고 조회 테스트: 전체조회 성공")
    void findPostReportListTest() {

        //given
        postReportCommandService.savePostReport(new PostReportRequest(1, "욕설"));
        postReportCommandService.savePostReport(new PostReportRequest(2, "홍보"));
        postReportCommandService.savePostReport(new PostReportRequest(3, "도배"));

        //when
        List<PostReport> postReportList = postReportCommandRepository.findAll();

        //then
        Assertions.assertEquals(3, postReportList.size());
    }


    @Test
    @DisplayName("게시글 신고 삭제 테스트: 삭제 성공 확인")
    void deletePostReportTest() {

        //given
        PostReportRequest postReportRequest = new PostReportRequest(2, "홍보 게시물");
        PostReport savedPostReport = postReportCommandService.savePostReport(postReportRequest);

        Long before = postReportCommandRepository.count();

        //when
        postReportCommandService.deletePostReport(savedPostReport.getPostReportNo());

        Long after = postReportCommandRepository.count();

        //then
        Assertions.assertEquals(-1, after - before);
    }

    @Test
    @DisplayName("게시글 신고 삭제 테스트: 해당하는 신고 내역 없을 시 예외처리 확인")
    void checkPostReportNoExistTest() {

        //given
        PostReportRequest postReportRequest = new PostReportRequest(2, "홍보 게시물");
        PostReport savedPostReport = postReportCommandService.savePostReport(postReportRequest);

        Long before = postReportCommandRepository.count();

        //when
        assertThatThrownBy(() -> postReportCommandService.deletePostReport(10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당하는 신고 내역이 없습니다.");

        Long after = postReportCommandRepository.count();

        //then
        Assertions.assertEquals(after, before);
    }



}