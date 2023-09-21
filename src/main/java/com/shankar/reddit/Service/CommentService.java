package com.shankar.reddit.Service;

import com.shankar.reddit.Mapper.CommentMapper;
import com.shankar.reddit.dto.CommentDTO;
import com.shankar.reddit.entity.Comment;
import com.shankar.reddit.entity.NotificationEmail;
import com.shankar.reddit.entity.Post;
import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.exception.SpringRedditException;
import com.shankar.reddit.repo.CommentRepository;
import com.shankar.reddit.repo.PostRepository;
import com.shankar.reddit.repo.ReddituserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final ReddituserRepository userRepo;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {

        try {
            Post post = postRepo.findById(commentDTO.getPostId()).orElseThrow(() -> new SpringRedditException("No post found for this comment"));
            RedditUser user = authService.getCurrentUser();
            Comment savedComment = commentRepo.save(commentMapper.mapDtoToComment(commentDTO, post, user));
            String message = mailContentBuilder.build(post.getUser().getUsername() + " has commented on your post. " + post.getUrl());
            sendCommentNotification(message, post.getUser());
            return commentMapper.mapToCommentDTO(savedComment);
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }

    }

    private void sendCommentNotification(String message, RedditUser user) {
        mailService.sendEmail(new NotificationEmail(user.getUsername()+" is commented on your post ", user.getEmail(),message));
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsForPost(Long id) {

        try {
            Post post = postRepo.findById(id).orElseThrow(() -> new SpringRedditException("No post found for this comment"));
            return commentRepo.findByPost(post)
                    .stream()
                    .map(commentMapper::mapToCommentDTO)
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsByUser(String name) {

        try {
            RedditUser user = userRepo.findByUsername(name).orElseThrow(() -> new SpringRedditException("No user found for this comment"));
            return commentRepo.findAllByUser(user)
                    .stream()
                    .map(commentMapper::mapToCommentDTO)
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new SpringRedditException(e.getMessage());
        }
    }
}
