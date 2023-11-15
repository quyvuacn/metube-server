package org.aptech.metube.videoservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.aptech.metube.videoservice.config.Translator;
import org.aptech.metube.videoservice.controller.request.GetVideoByCategoryIdRequest;
import org.aptech.metube.videoservice.controller.request.UpdateVideoStatusRequest;
import org.aptech.metube.videoservice.controller.request.VideoCreateRequest;
import org.aptech.metube.videoservice.controller.response.ApiResponse;
import org.aptech.metube.videoservice.controller.response.VideoResponse;
import org.aptech.metube.videoservice.dto.VideoDto;
import org.aptech.metube.videoservice.entity.Video;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.exception.RequestValidException;
import org.aptech.metube.videoservice.mapper.VideoMapper;
import org.aptech.metube.videoservice.service.VideoService;
import org.aptech.metube.videoservice.service.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.*;

/*
    @author: Dinh Quang Anh
    Date   : 8/23/2023
    Project: metube-server
*/
@RestController
@RequestMapping("/api/v1/video")
@Slf4j
public class VideoController extends BaseController {
    @Autowired
    VideoService videoService;
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    // save video
    @PostMapping("/save-video")
    public ApiResponse save(@RequestBody VideoCreateRequest request) throws NotFoundException {
        Video result = videoService.saveVideoInfo(request);
        return new ApiResponse(OK, Translator.toLocale("video.create.success"), result);  // không trả message như này
    }

    @GetMapping("/get")
    public ApiResponse getById(@RequestParam Long id, HttpServletRequest request) throws NotFoundException {
        Long startTime = System.currentTimeMillis();
        if (id == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        List<VideoResponse> result = videoService.getVideoById(id, request);
        Long processTime = System.currentTimeMillis() - startTime;
        System.out.println(processTime);
        return new ApiResponse(OK, Translator.toLocale("video.get.success"), result);
    }

    // delete video
    @GetMapping("/delete")
    public ApiResponse deleteVideo(@RequestBody Long id){
        if (id == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        if (videoService.getCurrentUser().getId() == null){
            throw new RequestValidException(Translator.toLocale("user.not.found"));
        }
        videoService.deleteVideo(id);
        return new ApiResponse(OK, Translator.toLocale("video.delete.success"));
    }

    // update video
    @PostMapping("/update-video")
    public ApiResponse updateVideo(@RequestBody VideoDto request){
        if (videoService.getCurrentUser().getId() == null){
            throw new RequestValidException(Translator.toLocale("user.not.found"));
        }
        Video result = videoService.updateVideo(request);
            return new ApiResponse(OK, "Success", result);
    }

    @PutMapping("/update-status")
    public ApiResponse updateStatus(@RequestBody UpdateVideoStatusRequest request){
        if (request.getUserId() == null || request.getStatusCode() == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        ApiResponse result = videoService.updateVideoStatus(request);
        return new ApiResponse(OK, Translator.toLocale("Success"), result);
    }

    // get recommend videos
    @GetMapping("/list/recommend")
    public ApiResponse getRecommend() {
        List<Video> videos = videoService.getRecommendVideo();
        return new ApiResponse(OK, Translator.toLocale("get.recommend.success"), videos);
    }

    // Get list video
    @GetMapping("/list-video")
    public ApiResponse getVideos(@RequestParam(required = false) String search,
                                 @RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize){
        List<VideoResponse> result = videoService.getListVideo(search, pageNum, pageSize);
        return new ApiResponse(OK, Translator.toLocale("video.get.success"), result);
    }
    @GetMapping("/search-history")
    public ApiResponse getSearchHistory (@RequestParam(required = false) String search){
        List<String> result = videoService.searchCacheContains(search);
        return new ApiResponse(OK, Translator.toLocale("get.search-history.success"), result);
    }

    @GetMapping("/list-video/category/{id}")
    public ApiResponse getVideosByCategoryId(@PathVariable Long id) throws NotFoundException {
        if (id == null){
            throw new RequestValidException(Translator.toLocale("category.not.found"));
        }
        List<VideoResponse> result = videoService.getVideosByCategoryId(id);
        return new ApiResponse(OK, Translator.toLocale("get-video.by.category.success"), result);
    }

    @GetMapping("/user/{id}")
    public ApiResponse getVideos(@PathVariable Long id,
                                 @RequestParam(defaultValue = "0") Integer pageNum,
                                 @RequestParam(defaultValue = "12") Integer pageSize,
                                 HttpServletRequest request) throws NotFoundException {
        if (id == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        Map<String, Object> result = videoService.findAllByUserId(id, pageNum, pageSize, request);
        return new ApiResponse(OK, Translator.toLocale("video.get.success"), result);
    }
    @GetMapping("/user/latest/{id}")
    public ApiResponse getLatestVideo(@PathVariable Long id, HttpServletRequest request) throws NotFoundException {
        if (id == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        VideoResponse result = videoService.getLatestVideoByUserId(id, request);
        return new ApiResponse(OK, Translator.toLocale("video.get-latest.success"), result);
    }

    @PostMapping("/countView/{id}")
    public ApiResponse pushView(@PathVariable Long id) throws NotFoundException {
        if (id == null){
            throw new NotFoundException(Translator.toLocale("not.found.video"));
        }
        videoService.countView(id);
        return new ApiResponse(OK, Translator.toLocale("success"));
    }

    // get video information by id
    @GetMapping("/item")
    public ApiResponse getVideoById(@RequestParam(required = false) long id, HttpServletRequest request) throws NotFoundException {
        List<VideoResponse> videoDto = videoService.getVideoById(id, request);
        return new ApiResponse(OK, Translator.toLocale("video.get-by-id.success"), videoDto);
    }

//    @GetMapping("/stream/{fileType}/{fileName}")
//    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
//                                                    @PathVariable("fileType") String fileType,
//                                                    @PathVariable("fileName") String fileName) {
//        return Mono.just(videoService.prepareContent(fileName, fileType, httpRangeList));
//    }

    @GetMapping("/testRedis")
    public Object testAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        redisTemplate.opsForValue().set("userCache:" + userDetails.getUsername(), userDetails, 600000, TimeUnit.MILLISECONDS);
        return authentication.getPrincipal();
    }
}
