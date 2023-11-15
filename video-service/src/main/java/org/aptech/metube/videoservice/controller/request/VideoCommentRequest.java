package org.aptech.metube.videoservice.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoCommentRequest {
    private Long videoOwnerId;
    private Long parentCommentId;
    private String content;
    private Long videoId;
}
