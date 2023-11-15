package org.aptech.metube.videoservice.service;

import org.aptech.metube.videoservice.controller.request.VideoCommentRequest;
import org.aptech.metube.videoservice.controller.request.VideoCommentUpdateRequest;
import org.aptech.metube.videoservice.entity.VideoComment;
import org.aptech.metube.videoservice.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

public interface VideoCommentService {
    Long save(VideoCommentRequest request, HttpServletRequest httpServletRequest) throws NotFoundException;
    Long update (VideoCommentUpdateRequest request) throws NotFoundException;
    void delete (Long id) throws NotFoundException;
    LinkedHashMap<String, Object> findAllByVideoId(Long id, Integer pageNum, Integer pageSize);
}
