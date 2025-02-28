package com.green.greengramver.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="피드 댓글 등록 요청")
@EqualsAndHashCode
public class FeedCommentPostReq {
    @Schema(title="피드 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @JsonIgnore
    private long userId;
    @Schema(title="댓글 내용", example = "댓글입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;

    @JsonIgnore
    private long feedCommentId;
}
