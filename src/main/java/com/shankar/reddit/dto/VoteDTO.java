package com.shankar.reddit.dto;

import com.shankar.reddit.entity.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDTO {

    private VoteType voteType;
    private Long postId;
}
