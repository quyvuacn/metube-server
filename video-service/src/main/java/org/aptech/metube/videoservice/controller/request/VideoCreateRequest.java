package org.aptech.metube.videoservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import org.aptech.metube.videoservice.entity.Category;
import org.aptech.metube.videoservice.entity.FavouriteList;
import org.aptech.metube.videoservice.entity.ListVideo;
import org.aptech.metube.videoservice.entity.VideoComment;

import java.util.List;

@Getter
@Setter
public class VideoCreateRequest {
    private long id;
    private Boolean isAds;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private String description;
    private int countLike;
    private int countDislike;
    private int countView;
    private int userId;
    private List<Long> favouriteListIds;
    private List<Long> listVideoUploadIds;
    private List<Long> videoCommentIds;
    private List<Long> categoryIds;
}
