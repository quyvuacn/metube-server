package org.aptech.metube.videoservice.repo;

import org.aptech.metube.videoservice.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {
    Page<Video> findAllByUserId(Long userId, Pageable pageable);
    @Query(value = "SELECT * FROM video WHERE user_id = ?1 ORDER BY created_date DESC LIMIT 1", nativeQuery = true)
    Video getLatestVideoByUserId(Long userId);
    @Query(value = "select * from video v join category_video cv Where v.id = cv.video_id and cv.category_id = :categoryId and v.is_ads = false", nativeQuery = true)
    List<Video> findVideosByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "select * from video v Where v.user_id = :userId", nativeQuery = true)
    List<Video> findVideosByUserId(Long userId);
    @Query(value = "select * from video v where v.is_ads = :isAds order by Rand() limit 1", nativeQuery = true)
    Video getVideoByIsAdsIs(@Param("isAds") Boolean isAds);
}