package com.shankar.reddit.Service;

import com.shankar.reddit.Mapper.PostMapper;
import com.shankar.reddit.dto.PostRequest;
import com.shankar.reddit.dto.PostResponse;
import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.entity.SubReddit;
import com.shankar.reddit.exception.SpringRedditException;
import com.shankar.reddit.repo.PostRepository;
import com.shankar.reddit.repo.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final SubredditRepository subredditRepo;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepo;

    @Transactional
    public PostResponse savePost(PostRequest postRequest) {
        SubReddit subReddit = subredditRepo.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException("SubReddit not found " + postRequest.getSubredditName()));
        RedditUser user = authService.getCurrentUser();

       return postMapper.mapToPostDto(postRepo.save(postMapper.mapDtoToPost(postRequest,subReddit,user)));


    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        try {
            return postRepo.findAll()
                    .stream()
                    .map(postMapper::mapToPostDto)
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }

    }


    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        try {
            Post post = postRepo.findById(postId)
                    .orElseThrow(() -> new SpringRedditException("Post not found for the id " + postId));
            return postMapper.mapToPostDto(post);
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }

    }


    public List<PostResponse> getPostBysubredditId(long id) {

        try {
            SubReddit subReddit = subredditRepo.findById(id).orElseThrow(() -> new SpringRedditException(" SubReddit not found for the id " + id));
            List<Post> allBySubReddit = postRepo.findAllBySubReddit(subReddit);
            return allBySubReddit.stream()
                    .map(postMapper::mapToPostDto)
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }


    }

    public List<PostResponse> getPostsByUsername(String username) {
        try {
            RedditUser user = authService.getCurrentUser();
            log.info("Current user "+user.getUsername());
            return postRepo.findByUser(user)
                    .stream()
                    .map(postMapper::mapToPostDto)
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }
    }
}
