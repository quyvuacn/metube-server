package org.aptech.metube.personal.repository;

import org.aptech.metube.personal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findUserByEmail(String email);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    List<User> findAllByRolesContains(Long id);
}
