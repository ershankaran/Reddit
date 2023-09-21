package com.shankar.reddit.Controller;

import com.shankar.reddit.Service.SubredditService;
import com.shankar.reddit.dto.SubRedditDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubRedditDTO> createSubReddit(@RequestBody SubRedditDTO subRedditDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subRedditDTO));
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDTO>> getAllSubReddits(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditDTO> getSubReddit(@PathVariable("id") long subRedditId){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubReddit(subRedditId));
    }
}
