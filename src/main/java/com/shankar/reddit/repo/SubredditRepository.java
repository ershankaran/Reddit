package com.shankar.reddit.repo;

import com.shankar.reddit.entity.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<SubReddit,Long> {

    Optional<SubReddit> findByName(String subredditName);

}
