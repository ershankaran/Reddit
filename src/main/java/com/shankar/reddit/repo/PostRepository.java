package com.shankar.reddit.repo;

import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.entity.SubReddit;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllBySubReddit(SubReddit subReddit);
    List<Post> findByUser(RedditUser user);
}
