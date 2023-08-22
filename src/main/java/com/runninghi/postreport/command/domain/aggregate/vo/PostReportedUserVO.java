package com.runninghi.postreport.command.domain.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@ToString
public class PostReportedUserVO implements Serializable {

    @Column
    private UUID postReportedUserNo;

    public PostReportedUserVO(UUID postReportedUserNo) {
        this.postReportedUserNo = postReportedUserNo;
    }
}
