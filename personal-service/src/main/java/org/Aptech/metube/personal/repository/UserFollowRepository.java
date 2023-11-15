package org.aptech.metube.personal.repository;

import org.aptech.metube.personal.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    @Query(value = "select uf.follower_id from user_follow uf Where uf.user_id = :userId", nativeQuery = true)
    List<Long> findFollowerIdByUserId(@Param("userId") Long userId);
    @Query(value = "select * from user_follow uf where uf.user_id = :userId and uf.follower_id = :followerId", nativeQuery = true)
    UserFollow findByUserIdAndFollowerId(@Param("userId") Long userId, @Param("followerId") Long followerId);
}
