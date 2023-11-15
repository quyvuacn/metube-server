package org.aptech.metube.videoservice.dto;

import org.aptech.metube.videoservice.entity.Video;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FavouriteListDto {
    private long id;
    private long userId;
    private List<Video> videos = new ArrayList<>();
}
