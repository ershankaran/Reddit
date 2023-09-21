package com.shankar.reddit.repo;

import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

    Optional<Vote> findByPostAndUserOrderByVoteIdDesc(Post post, RedditUser user);
}
