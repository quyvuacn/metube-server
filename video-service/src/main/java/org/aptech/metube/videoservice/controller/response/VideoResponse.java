package org.aptech.metube.videoservice.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aptech.metube.videoservice.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class VideoResponse {
    private Long id;
    private Boolean isAds;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private String description;
    private int countLike;
    private int countDislike;
    private int countView;
    private Long userId;
    private LocalDateTime createdDate;
    private List<Category> categories;
}
