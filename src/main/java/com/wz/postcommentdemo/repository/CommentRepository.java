package com.wz.postcommentdemo.repository;

import com.wz.postcommentdemo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByPostIdOrderByPublishedAtDesc(@Param("postId") Long postId, Pageable pageable);
}
