package org.aptech.metube.videoservice.service;

import org.aptech.metube.videoservice.controller.request.UpdateVideoStatusRequest;
import org.aptech.metube.videoservice.controller.request.VideoCreateRequest;
import org.aptech.metube.videoservice.controller.response.ApiResponse;
import org.aptech.metube.videoservice.controller.response.VideoResponse;
import org.aptech.metube.videoservice.dto.VideoDto;
import org.aptech.metube.videoservice.entity.Video;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.service.impl.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/*
    @author: Dinh Quang Anh
    Date   : 8/22/2023
    Project: metube-server
*/
public interface VideoService {
    UserDetailsImpl getCurrentUser();
    Video updateVideo(VideoDto videoDto);
    void deleteVideo(Long id);

    List<VideoResponse> getListVideo(String search, Integer pageNum, Integer pageSize);

    List<VideoResponse> getVideoById(long id, HttpServletRequest request) throws NotFoundException;

    Video saveVideoInfo(VideoCreateRequest request) throws NotFoundException;

//    ResponseEntity<byte[]> prepareContent(String fileName, String fileType, String httpRangeList);
    Map<String, Object> findAllByUserId(Long userId, Integer pageNum, Integer pageSize, HttpServletRequest request) throws NotFoundException;
    VideoResponse getLatestVideoByUserId(Long userId, HttpServletRequest request) throws NotFoundException;
    List<VideoResponse> getVideosByCategoryId(Long categoryId) throws NotFoundException;
    List<String> searchCacheContains(String searchString);
    List<Video> getRecommendVideo();
    ApiResponse updateVideoStatus(UpdateVideoStatusRequest request);

    void countView(Long videoId);
}
