package com.runninghi.image.command.domain.repository;

import com.runninghi.image.command.domain.aggregate.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageCommandRepository extends JpaRepository<Image, Long> {

    // 특정 유저 게시물과 관련된 이미지들 전부 삭제
    void deleteImagesByUserPostVO_UserPostNo(Long userPostNo);

    // 특정 유저 게시물과 관련된 이미지 url list 조회
    @Query(value = "SELECT i.image_url FROM tbl_image i WHERE i.user_post_no = :userPostNo", nativeQuery = true)
    List<String> getImagesByUserPostNo(@Param("userPostNo") Long userPostNo);

    // 특정 관리자 게시물과 관련된 이미지들 전부 삭제
    void deleteImagesByAdminPostVO_AdminPostNo(Long adminPostNo);

    // 특정 관리자 게시물과 관련된 이미지 url list 조회
    @Query(value = "SELECT i.image_url FROM tbl_image i WHERE i.admin_post_no = :adminPostNo", nativeQuery = true)
    List<String> getImagesByAdminPostNo(@Param("adminPostNo") Long adminPostNo);
}
