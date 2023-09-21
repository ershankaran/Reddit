package com.shankar.reddit.Mapper;

import com.shankar.reddit.dto.CommentDTO;
import com.shankar.reddit.entity.Comment;
import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    CommentDTO mapToCommentDTO(Comment comment);

    @Mapping(target = "text" ,source = "commentDTO.text")
    @Mapping(target = "post" ,source = "post")
    @Mapping(target = "user" , source = "user")
    @Mapping(target = "createdDate" , expression = "java(java.time.Instant.now())")
    Comment mapDtoToComment(CommentDTO commentDTO, Post post, RedditUser user);
}
