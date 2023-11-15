package org.aptech.metube.personal.repository;

import org.aptech.metube.personal.entity.UserAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAccountTypeRepository extends JpaRepository<UserAccountType, Long> {
    @Query(value = "SELECT * FROM user_account_type uat WHERE uat.user_id = :userId", nativeQuery = true)
    List<UserAccountType> findByUserId(@Param("userId") Long userId);}
