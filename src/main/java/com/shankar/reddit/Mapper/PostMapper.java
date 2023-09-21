package com.shankar.reddit.Mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.shankar.reddit.Service.AuthService;
import com.shankar.reddit.dto.PostRequest;
import com.shankar.reddit.dto.PostResponse;
import com.shankar.reddit.entity.*;
import com.shankar.reddit.repo.CommentRepository;
import com.shankar.reddit.repo.VoteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.shankar.reddit.entity.VoteType.DOWNVOTE;
import static com.shankar.reddit.entity.VoteType.UPVOTE;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "CreatedDate" , expression = "java(java.time.Instant.now())")
    @Mapping(target ="subReddit" ,source = "subReddit")
    @Mapping(target = "user",source = "user")
    @Mapping(target = "description",source = "postRequest.description")
    public abstract Post mapDtoToPost(PostRequest postRequest, SubReddit subReddit, RedditUser user);

    @Mapping(target = "id",source = "postId")
    @Mapping(target = "userName",source = "user.username")
    @Mapping(target="subredditName",source = "subReddit.name")
    @Mapping(target = "commentCount" , expression = "java(CommentCount(post))")
    @Mapping(target = "duration" , expression = "java(getDuration(post))")
    @Mapping(target = "upVote" , expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote" , expression = "java(isPostDownVoted(post))")
     public abstract PostResponse mapToPostDto(Post post);

    Integer CommentCount(Post post){
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    Boolean isPostUpVoted(Post post){
        return checkVoteType(post,UPVOTE);
    }

    Boolean isPostDownVoted(Post post){
        return checkVoteType(post,DOWNVOTE);
    }

    private Boolean checkVoteType(Post post, VoteType voteType){
        if(authService.isLoggedIn()){
            Optional<Vote> voteForPostByUser = voteRepository.findByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

}
