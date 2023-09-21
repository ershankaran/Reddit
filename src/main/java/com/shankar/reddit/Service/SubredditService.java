package com.shankar.reddit.Service;

import com.shankar.reddit.Mapper.SubredditMapper;
import com.shankar.reddit.dto.SubRedditDTO;
import com.shankar.reddit.entity.SubReddit;
import com.shankar.reddit.exception.SpringRedditException;
import com.shankar.reddit.repo.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepo;

    private final SubredditMapper subredditMapper;

    @Transactional
    public SubRedditDTO save(SubRedditDTO subRedditDTO) {
        try {
            SubReddit savedSubReddit = subredditRepo.save(subredditMapper.mapDtoToSubReddit(subRedditDTO));

            return subredditMapper.mapSubredditToDTO(savedSubReddit);
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public SubReddit mapDTOtoSubReddit(SubRedditDTO subRedditDTO) {

        return SubReddit.builder()
                .name(subRedditDTO.getName())
                .description(subRedditDTO.getDescription())
                .createdDate(Instant.now())
//                .posts(subRedditDTO.getNumberOfPosts())
                .build();
    }

    @Transactional(readOnly = true)
    public SubRedditDTO maptoSubRedditDTO(SubReddit subReddit) {

        return SubRedditDTO.builder()
                .id(subReddit.getId())
                .name(subReddit.getName())
                .description(subReddit.getDescription())
//                .posts(subRedditDTO.getNumberOfPosts())
                .build();
    }

    public List<SubRedditDTO> getAll() {
       try {
           return subredditRepo.findAll()
                   .stream()
                   .map(subredditMapper::mapSubredditToDTO)
                   .collect(Collectors.toList());

       } catch (Exception e){
           throw new SpringRedditException(e.getMessage());
       }


    }

    public SubRedditDTO getSubReddit(long subRedditId) {
        try{
            SubReddit subReddit = subredditRepo.findById(subRedditId).orElseThrow(() -> new SpringRedditException("Subreddit not found"));
            return subredditMapper.mapSubredditToDTO(subReddit);
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }


    }
}
