package org.aptech.metube.videoservice.specification;

import org.aptech.metube.videoservice.constant.EntityStatusCode;
import org.aptech.metube.videoservice.entity.Video;
import org.springframework.data.jpa.domain.Specification;

public class VideoSpecification {
    public static Specification<Video> hasTitle(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search != null && !search.isEmpty()) {
                return criteriaBuilder.like(root.get("title"), "%" + search + "%");
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
    public static Specification<Video> hasIsAds(Boolean isAds) {
        return (root, query, criteriaBuilder) -> {
            if (isAds != null) {
                return criteriaBuilder.equal(root.get("isAds"), isAds);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
    public static Specification<Video> hasStatusActive(EntityStatusCode statusCode) {
        return (root, query, criteriaBuilder) -> {
            if (statusCode != null) {
                return criteriaBuilder.equal(root.get("statusCode"), statusCode);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}
