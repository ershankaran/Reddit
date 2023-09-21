package com.shankar.reddit.Controller;

import com.shankar.reddit.Service.VoteService;
import com.shankar.reddit.dto.VoteDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vote")
@Slf4j
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity saveVote(@RequestBody  VoteDTO voteDTO){
        log.info("Vote Controller");
        return ResponseEntity.status(HttpStatus.CREATED).body(voteService.saveVote(voteDTO));
    }

}
