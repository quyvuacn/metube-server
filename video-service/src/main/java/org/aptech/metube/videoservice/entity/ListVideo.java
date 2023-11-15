package org.aptech.metube.videoservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "list_video")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "user_id")
    private long userID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "list_video_upload",
            joinColumns = @JoinColumn(name = "list_video_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id"))
    private List<Video> videos;
}
