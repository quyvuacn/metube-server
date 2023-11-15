package org.aptech.metube.videoservice.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "favourite_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FavouriteList{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    private long userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "video_favourite",
            joinColumns = @JoinColumn(name = "favourite_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id"))
    private List<Video> videos;
}
