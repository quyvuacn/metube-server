package org.aptech.metube.videoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aptech.metube.videoservice.constant.EntityStatusCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "video")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "isAds")
    private Boolean isAds;
    @Column(name = "title")
    private String title;
    @Column(name = "video_url")
    private String videoUrl;
    @Column(name = "thumbnail")
    private String thumbnail;
    @Column(name = "description")
    private String description;
    @Column(name = "count_like")
    private int countLike;
    @Column(name = "count_dislike")
    private int countDislike;
    @Column(name = "count_view")
    private int countView;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "status")
    private EntityStatusCode statusCode;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


    @PrePersist
    private void prePersist() {
        setCreatedDate(LocalDateTime.now());
    }

    @PreUpdate
    private void preUpdate() {
        setModifiedDate(LocalDateTime.now());
    }

    @ManyToMany(mappedBy = "videos", fetch = FetchType.EAGER)
    List<FavouriteList> favouriteLists;
    //
//    @ManyToMany(mappedBy = "videos")
//    List<ListVideo> listVideoUploads;
//
    @JsonIgnoreProperties("videos")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_video", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    //
    @JsonIgnoreProperties("video")
    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<VideoComment> videoComments;
}
