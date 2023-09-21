package com.shankar.reddit.repo;

import com.shankar.reddit.entity.RedditUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReddituserRepository extends JpaRepository<RedditUser,Long> {

    Optional<RedditUser> findByUsername(String username);
}
