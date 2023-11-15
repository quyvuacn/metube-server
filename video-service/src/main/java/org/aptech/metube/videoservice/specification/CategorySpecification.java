package org.aptech.metube.videoservice.specification;

import org.aptech.metube.videoservice.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> hasName(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search != null && !search.isEmpty()) {
                return criteriaBuilder.like(root.get("categoryName"), "%" + search + "%");
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}
