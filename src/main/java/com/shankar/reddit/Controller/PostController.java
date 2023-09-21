package com.shankar.reddit.Controller;

import com.shankar.reddit.Service.PostService;
import com.shankar.reddit.dto.PostRequest;
import com.shankar.reddit.dto.PostResponse;
import com.shankar.reddit.entity.Post;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(postRequest));

    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(postId));
    }

    @GetMapping(params = "subRedditId")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@RequestParam("subRedditId") long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostBysubredditId(id));
    }

    @GetMapping(params = "username")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@RequestParam("username") String username){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }


}
