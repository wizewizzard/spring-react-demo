package com.wz.postcommentdemo.service;

import com.wz.postcommentdemo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PostService {
    Post createNewPost(Post post, MultipartFile image);
    Post editPost(Long postId, Post post, MultipartFile image);
    Optional<Post> getPostById(Long postId);
    Page<Post> getPostPage(Pageable p);
    void deletePostById(Long postId);
}
