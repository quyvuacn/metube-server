package org.aptech.metube.videoservice.controller.request;

import lombok.Data;
import org.aptech.metube.videoservice.entity.Video;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadVideoRequest {
    private MultipartFile video;
    private String title;
    private String thumbnail;
    private String description;
}
