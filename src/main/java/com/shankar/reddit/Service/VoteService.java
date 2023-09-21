package com.shankar.reddit.Service;

import com.shankar.reddit.dto.VoteDTO;
import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.entity.Vote;
import com.shankar.reddit.exception.SpringRedditException;
import com.shankar.reddit.repo.PostRepository;
import com.shankar.reddit.repo.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.shankar.reddit.entity.VoteType.UPVOTE;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepo;
    private final PostRepository postRepo;
    private final AuthService authService;

    @Transactional
    public VoteDTO saveVote(VoteDTO voteDTO) {
        log.info("Vote Service");
        log.info(voteDTO.getVoteType()+" "+voteDTO.getPostId());
        try{
            Post post = postRepo.findById(voteDTO.getPostId()).orElseThrow(() -> new SpringRedditException("No post found for this comment"));
            RedditUser user = authService.getCurrentUser();
            Optional<Vote> vote = voteRepo.findByPostAndUserOrderByVoteIdDesc(post, user);
            if(vote.isPresent() && vote.get().getVoteType().equals(voteDTO.getVoteType())){
                throw new SpringRedditException("You already casted Vote for this post");
            }

            if(UPVOTE.equals(voteDTO.getVoteType())){
                post.setVoteCount(post.getVoteCount()+1);
            } else {
                post.setVoteCount(post.getVoteCount()-1);
            }

            Vote savedVote = voteRepo.save(mapToVote(voteDTO, post));

            return mapVoteToDTO(savedVote);
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }
    }

    private VoteDTO mapVoteToDTO(Vote savedVote) {
        return VoteDTO.builder()
                .voteType(savedVote.getVoteType())
                .postId(savedVote.getPost().getPostId())
                .build();
    }

    private Vote mapToVote(VoteDTO voteDTO, Post post) {
        return  Vote.builder()
                .voteType(voteDTO.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
