package com.shankar.reddit.repo;

import com.shankar.reddit.entity.Comment;
import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(RedditUser user);
}
