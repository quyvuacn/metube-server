package org.aptech.metube.videoservice.dto;

import org.aptech.metube.videoservice.entity.Video;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ListVideoDto {
    private long id;
    private String name;
    private long userID;
    private Set<Video> videos = new HashSet<>();
}
