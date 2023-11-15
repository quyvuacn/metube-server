package org.aptech.metube.videoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "video_comment")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VideoComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "parent_id")
    private Long parentCommentId;

    @JsonIgnoreProperties("videoComments")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Video video;
}
