package org.aptech.metube.videoservice.dto;

import org.aptech.metube.videoservice.entity.Video;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {
    private long id;
    private String categoryName;
    private List<Video> videos = new ArrayList<>();
}
