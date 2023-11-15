package org.aptech.metube.personal.service;

import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.entity.UserFollow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFollowService {
    Long save(UserFollow userFollow) throws NotFoundException;
    Boolean checkFollowedUser(Long id);
}
