package org.aptech.metube.videoservice.service.impl;

import org.aptech.metube.videoservice.config.RestTemplateConfig;
import org.aptech.metube.videoservice.config.Translator;
import org.aptech.metube.videoservice.controller.request.VideoCommentRequest;
import org.aptech.metube.videoservice.controller.request.VideoCommentUpdateRequest;
import org.aptech.metube.videoservice.controller.response.UserResponse;
import org.aptech.metube.videoservice.entity.Video;
import org.aptech.metube.videoservice.entity.VideoComment;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.exception.RequestValidException;
import org.aptech.metube.videoservice.repo.VideoCommentRepository;
import org.aptech.metube.videoservice.repo.VideoRepository;
import org.aptech.metube.videoservice.security.utils.JwtUtils;
import org.aptech.metube.videoservice.service.VideoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class VideoCommentServiceImpl implements VideoCommentService {
    @Autowired
    VideoCommentRepository videoCommentRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    RestTemplate restTemplate;
    @Value("${base.url}")
    private String apiBaseUrl;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public Long save(VideoCommentRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        if (request.getVideoId() == null || request.getContent() == null || request.getVideoOwnerId() == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Video video = videoRepository.findById(request.getVideoId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("video.not.found")));
        VideoComment videoComment = VideoComment.builder()
                .video(video)
                .parentCommentId(request.getParentCommentId())
                .content(request.getContent())
                .userId(userDetails.getId().longValue())
                .build();
        videoCommentRepository.save(videoComment);
        // todo: push notify
//        if (request.getParentCommentId() == null){
//            VideoComment videoCommentParent = videoCommentRepository.findById(request.getParentCommentId())
//                    .orElseThrow(() -> new NotFoundException(Translator.toLocale("video-comment.not.found")));
//            UserResponse userResponse = getUserById(videoCommentParent.getUserId(), httpServletRequest);
//
//        }


        return videoComment.getId();
    }

    @Override
    public Long update(VideoCommentUpdateRequest request) throws NotFoundException {
        VideoComment videoComment = videoCommentRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("video-comment.not.found")));
        videoComment.setContent(request.getContent());
        videoCommentRepository.save(videoComment);
        return videoComment.getId();
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        VideoComment videoComment = videoCommentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("video-comment.not.found")));
        List<VideoComment> childComment = videoCommentRepository.findVideoCommentsByParentCommentId(videoComment.getId());
        if (!CollectionUtils.isEmpty(childComment)){
            videoCommentRepository.deleteAll(childComment);
        }
        videoCommentRepository.delete(videoComment);
    }

    @Override
    public LinkedHashMap<String, Object>  findAllByVideoId(Long id, Integer pageNum, Integer pageSize) {
        if (pageNum == null)
            pageNum = 0;
        if (pageSize == null)
            pageSize = 20;
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<VideoComment> videoComments = videoCommentRepository.findAll(paging);

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("pageNum", videoComments.getNumber());
        result.put("pageSize", videoComments.getSize());
        result.put("totalPage", videoComments.getTotalPages());
        result.put("data", videoComments.getContent());

        return result;
    }


    private UserResponse getUserById(Long userId, HttpServletRequest request) throws NotFoundException {
        String apiUrl = apiBaseUrl + "/api/v1/users/get/" + userId;
        String token = jwtUtils.extractTokenFromRequest(request);

        ParameterizedTypeReference<Integer> requestType = new ParameterizedTypeReference<Integer>() {};
        ParameterizedTypeReference<Map<String,Object>> responseType = new ParameterizedTypeReference<>() {};
        Map<String, Object> result = RestTemplateConfig.callApiMethodGET(apiUrl, token, requestType, responseType, restTemplate);
        assert result != null;
        if (result.get("data") == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        return (UserResponse) result.get("data");
    }
}
