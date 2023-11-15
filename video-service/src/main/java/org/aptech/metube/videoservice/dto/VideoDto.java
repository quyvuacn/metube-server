package org.aptech.metube.videoservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.aptech.metube.videoservice.entity.Category;
import org.aptech.metube.videoservice.entity.FavouriteList;
import org.aptech.metube.videoservice.entity.ListVideo;
import org.aptech.metube.videoservice.entity.VideoComment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class VideoDto extends BaseDto{
    private long id;
    private boolean isAds;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private String description;
    private int countLike;
    private int countDislike;
    private int countView;
    private int userId;
    private LocalDateTime createdDate;
    private List<FavouriteList> favouriteLists;
    private List<ListVideo> listVideoUploads;
    private List<VideoComment> videoComments;
    private List<Category> categories;
}
