package org.aptech.metube.videoservice.repo;

import org.aptech.metube.videoservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    @Query(value = "select * from category c join category_video cv on c.id = cv.category_id and cv.video_id = :id", nativeQuery = true)
    List<Category> getCategoryByVideoId(@Param("id") Long id);

    @Query("SELECT c FROM Category c JOIN c.videos v WHERE v.id = :id")
    List<Category> findCategoriesByVideoId(@Param("id") Long id);

}
