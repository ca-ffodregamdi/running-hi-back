package com.runninghi.bookmark.command.application.service;

import com.runninghi.bookmark.command.application.dto.request.CreateBookmarkRequest;
import com.runninghi.bookmark.command.domain.aggregate.vo.BookmarkVO;
import com.runninghi.bookmark.command.domain.repository.BookmarkRepository;
import com.runninghi.bookmarkfolder.command.application.service.CreateNewBookmarkFolderService;
import com.runninghi.feedback.command.domain.exception.customException.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
@Transactional
public class CreateBookmarkTests {

    @Autowired
    private CreateBookmarkService createBookmarkService;

    @Autowired
    private BookmarkRepository bookmarkRepository;


    @Test
    @DisplayName("생성: 즐겨찾기 추가 기능")
    void testCreateBookmark() {

        Long beforeSize = bookmarkRepository.count();

        BookmarkVO bookmarkVO = new BookmarkVO(1L, 2L);
        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest(bookmarkVO, UUID.randomUUID());
        createBookmarkService.createBookmark(bookmarkRequest);

        Long afterSize = bookmarkRepository.count();

        Assertions.assertEquals(beforeSize + 1, afterSize);

    }

    @Test
    @DisplayName("생성: 존재하지 않는 즐겨찾기 폴더 번호 예외처리")
    void testFolderNoDoesntExist() {

        BookmarkVO bookmarkVO = new BookmarkVO(0L, 1L);
        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest(bookmarkVO, UUID.randomUUID());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> createBookmarkService.createBookmark(bookmarkRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 폴더가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("생성: 존재하지 않는 게시물 번호 예외처리")
    void testPostNoDoesntExist() {

//        BookmarkVO bookmarkVO = new BookmarkVO(1L, 0L);
//        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest(bookmarkVO, UUID.randomUUID());
//
//        org.assertj.core.api.Assertions.assertThatThrownBy(() -> createBookmarkService.createBookmark(bookmarkRequest))
//                .isInstanceOf(NotFoundException.class)
//                .hasMessage("존재하지 않는 게시물입니다.");
    }


}
