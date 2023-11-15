package org.aptech.metube.personal.specification;

import org.aptech.metube.personal.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasFullName(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search != null && !search.isEmpty()) {
                return criteriaBuilder.like(root.get("fullName"), "%" + search + "%");
            } else {
                return criteriaBuilder.conjunction(); // No filtering condition
            }
        };
    }

    public static Specification<User> hasEmail(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search != null && !search.isEmpty()) {
                return criteriaBuilder.like(root.get("email"), "%" + search + "%");
            } else {
                return criteriaBuilder.conjunction(); // No filtering condition
            }
        };
    }
}
