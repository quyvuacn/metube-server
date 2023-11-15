package org.aptech.metube.videoservice.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoCommentUpdateRequest {
    private Long id;
    private String content;
}
