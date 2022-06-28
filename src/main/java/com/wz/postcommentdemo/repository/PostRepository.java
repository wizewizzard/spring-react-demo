package com.wz.postcommentdemo.repository;

import com.wz.postcommentdemo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("from Post p join fetch p.comments where p.id=:postId")
    Optional<Post> findPostWithCommentsById(@Param("postId") Long postId);
}
