package org.aptech.metube.videoservice.controller;

import org.aptech.metube.videoservice.config.Translator;
import org.aptech.metube.videoservice.controller.request.VideoCommentRequest;
import org.aptech.metube.videoservice.controller.request.VideoCommentUpdateRequest;
import org.aptech.metube.videoservice.controller.response.ApiResponse;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.exception.RequestValidException;
import org.aptech.metube.videoservice.service.VideoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/video-comment")
public class VideoCommentController {
    @Autowired
    VideoCommentService videoCommentService;

    @PostMapping("/save")
    public ApiResponse saveComment(@RequestBody VideoCommentRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        if (Objects.isNull(request)){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        Long result = videoCommentService.save(request, httpServletRequest);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("success"), result);
    }

    @PutMapping("/update")
    public ApiResponse updateComment(@RequestBody VideoCommentUpdateRequest request) throws NotFoundException {
        if (request.getId() == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        Long result = videoCommentService.update(request);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("video-comment.update.success"), result);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteComment(@PathVariable("id") Long id) throws NotFoundException {
        if (id == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        videoCommentService.delete(id);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("delete.video-comment.success"));
    }

    @GetMapping("/get-all")
    public ApiResponse getListComment(@RequestParam Long id,
                                      @RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize
    ){
        if (id == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        LinkedHashMap<String, Object> result = videoCommentService.findAllByVideoId(id, pageNum, pageSize);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("get-all.video-comment.success"), result);
    }

}
