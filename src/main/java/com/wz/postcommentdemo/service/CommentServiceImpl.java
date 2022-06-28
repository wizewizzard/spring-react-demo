package com.wz.postcommentdemo.service;

import com.wz.postcommentdemo.entity.Comment;
import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.exception.exception.ResourceNotFoundException;
import com.wz.postcommentdemo.repository.CommentRepository;
import com.wz.postcommentdemo.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    @Override
    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with specified id was not found"));
        comment.setPublishedAt(LocalDateTime.now());
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getComments(Long postId, Pageable pageable) {
        return commentRepository.findCommentsByPostIdOrderByPublishedAtDesc(postId, pageable);
    }

    @Transactional
    @Override
    public void deleteComment(Long postId, Long commentId) {
        if(postRepository.existsById(postId)){
            commentRepository.deleteById(commentId);
        }
        else{
            throw new ResourceNotFoundException("Post with specified id was not found");
        }
    }
}
