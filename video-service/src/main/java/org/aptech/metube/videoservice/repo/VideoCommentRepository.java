package org.aptech.metube.videoservice.repo;

import org.aptech.metube.videoservice.entity.VideoComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoCommentRepository extends JpaRepository<VideoComment, Long> {
    List<VideoComment> findVideoCommentsByParentCommentId(Long id);
}
