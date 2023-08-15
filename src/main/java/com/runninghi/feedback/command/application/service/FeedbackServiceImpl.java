package com.runninghi.feedback.command.application.service;

import com.runninghi.feedback.command.application.dto.SaveFeedbackDTO;
import com.runninghi.feedback.command.domain.aggregate.entity.Feedback;
import com.runninghi.feedback.command.domain.aggregate.entity.FeedbackCategory;
import com.runninghi.feedback.command.domain.aggregate.vo.FeedbackWriterVO;
import com.runninghi.feedback.command.domain.exception.customException.IllegalArgumentException;
import com.runninghi.feedback.command.domain.repository.FeedbackRepository;
import com.runninghi.feedback.command.domain.service.FeedbackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // 피드백 저장
    @Override
    @Transactional
    public Long saveFeedback(SaveFeedbackDTO feedbackDTO, Long userNo) {

//        User writer = userRepository.findById(userNo)
//                .orElseThrow(() -> new NotFoundException("존재하지않는 회원입니다."));

        if (feedbackDTO.getFeedbackTitle().length() > 500) {
            throw new IllegalArgumentException("제목은 500자를 넘을 수 없습니다.");
        }

        if (feedbackDTO.getFeedbackTitle().length() == 0) {
            throw new IllegalArgumentException("제목은 1글자 이상이어야 합니다.");
        }

        if (feedbackDTO.getFeedbackContent().length() == 0) {
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }

        // 작성자 vo 생성
        FeedbackWriterVO feedbackWriterVO = new FeedbackWriterVO(userNo);

        Feedback feedback = new Feedback.Builder()
                .feedbackTitle(feedbackDTO.getFeedbackTitle())
                .feedbackContent(feedbackDTO.getFeedbackContent())
                .feedbackWriterVO(feedbackWriterVO)
                .feedbackCategory(FeedbackCategory.fromValue(feedbackDTO.getFeedbackCategory()))
                .build();

        // 피드백 저장
        feedbackRepository.save(feedback);

        return feedback.getFeedbackNo();

    }

}