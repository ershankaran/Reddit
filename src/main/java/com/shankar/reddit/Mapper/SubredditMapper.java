package com.shankar.reddit.Mapper;


import com.shankar.reddit.dto.SubRedditDTO;
import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.SubReddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts" , expression = "java(mapPosts(subReddit.getPosts()))")
    SubRedditDTO mapSubredditToDTO(SubReddit subReddit);

    default Integer mapPosts(List<Post> posts){
        return (posts != null) ? posts.size() : 0;
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    SubReddit mapDtoToSubReddit(SubRedditDTO subRedditDTO);
}
