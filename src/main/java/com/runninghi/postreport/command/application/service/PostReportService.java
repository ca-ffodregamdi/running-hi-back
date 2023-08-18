package com.runninghi.postreport.command.application.service;

import com.runninghi.feedback.command.domain.exception.customException.NotFoundException;
import com.runninghi.postreport.command.application.dto.RequestPostReportDTO;
import com.runninghi.postreport.command.domain.aggregate.entity.PostReport;
import com.runninghi.postreport.command.domain.aggregate.vo.PostReportUserVO;
import com.runninghi.postreport.command.domain.aggregate.vo.PostReportedUserVO;
import com.runninghi.postreport.command.domain.aggregate.vo.ReportedPostVO;
import com.runninghi.postreport.command.domain.repository.PostReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostReportService {

    private final PostReportRepository postReportRepository;

    public PostReport savePostReport(RequestPostReportDTO requestPostReportDTO) {

        if (requestPostReportDTO.getPostReportCategoryCode() == 0) {
            throw new IllegalArgumentException("신고 카테고리를 선택해주세요");
        }

        if (requestPostReportDTO.getPostReportContent().isEmpty()) {
            throw new IllegalArgumentException("신고 내용을 입력해주세요");
        }

        if (requestPostReportDTO.getPostReportContent().length() > 100) {
            throw new IllegalArgumentException("신고 내용은 100자를 넘을 수 없습니다.");
        }

        UUID reportUserNo = UUID.randomUUID();  //임의 값
        UUID reportedUserNo = UUID.randomUUID();
        Long reportedPostNo = 1L;

        PostReport postReport = PostReport.builder()
                .postReportCategoryCode(requestPostReportDTO.getPostReportCategoryCode())
                .postReportContent(requestPostReportDTO.getPostReportContent())
                .postReportedDate(LocalDate.now())
                .postReportUserVO(new PostReportUserVO(reportUserNo))
                .postReportedUserVO(new PostReportedUserVO(reportedUserNo))
                .reportedPostVO(new ReportedPostVO(reportedPostNo))
                .build();

        postReportRepository.save(postReport);

        return postReport;
    }


    public void deletePostReport(Long postReportNo) {

        if (postReportRepository.findById(postReportNo).isPresent()) {
            postReportRepository.deleteById(postReportNo);
        } else {
            throw new NotFoundException("해당하는 신고 내역이 없습니다.");
        }
    }
}