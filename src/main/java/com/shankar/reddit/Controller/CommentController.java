package com.shankar.reddit.Controller;

import com.shankar.reddit.Service.CommentService;
import com.shankar.reddit.dto.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentDTO));
    }


    @GetMapping(params="postId")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@RequestParam("postId") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(id));
    }

    @GetMapping(params = "username")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByUser(@RequestParam("username") String name){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByUser(name));
    }
}
