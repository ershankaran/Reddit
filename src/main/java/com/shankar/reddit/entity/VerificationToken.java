package com.shankar.reddit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.GenerationType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long Id;
    private String token;
    @OneToOne(fetch = LAZY)
    private RedditUser user;
    private Instant expiryDate;
}
