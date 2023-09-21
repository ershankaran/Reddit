package com.shankar.reddit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long postId;
    private String postName;
    private String url;
    @Lob
    private String description;
    // manually set the default value 0 at db level
    @Column(name = "vote_count")
    private Integer voteCount = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId",referencedColumnName = "userId")
    private RedditUser user;
    private Instant CreatedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id",referencedColumnName = "id")
    private SubReddit subReddit;
}
