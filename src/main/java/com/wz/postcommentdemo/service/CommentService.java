package com.wz.postcommentdemo.service;

import com.wz.postcommentdemo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment addComment(Long postId, Comment comment);
    Page<Comment> getComments(Long postId, Pageable pageable);

    void deleteComment(Long postId, Long commentId);
}
