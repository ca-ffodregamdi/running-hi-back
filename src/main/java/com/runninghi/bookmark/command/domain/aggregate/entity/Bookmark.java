package com.runninghi.bookmark.command.domain.aggregate.entity;

import com.runninghi.bookmark.command.domain.aggregate.vo.BookmarkVO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "TBL_BOOKMARK")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Bookmark implements Serializable {

    @EmbeddedId
    private BookmarkVO bookmark;

    @Column
    private Long userNo;

    public Bookmark(BookmarkVO bookmark) {
        this.bookmark = bookmark;
    }
    public Bookmark(Long folderNo, Long postNo){
        this.bookmark = new BookmarkVO(folderNo, postNo);
    }
}
