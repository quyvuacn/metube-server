package org.aptech.metube.videoservice.dto;

import org.aptech.metube.videoservice.entity.Video;
import lombok.Data;

@Data
public class VideoCommentDto {
    private long id;
    private String content;
    private long userId;
    private Video video;
}
