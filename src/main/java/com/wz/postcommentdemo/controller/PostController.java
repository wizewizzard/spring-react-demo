package com.wz.postcommentdemo.controller;

import com.wz.postcommentdemo.representation.dto.PostModelCut;
import com.wz.postcommentdemo.representation.dto.PostModel;
import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.exception.exception.ResourceNotFoundException;
import com.wz.postcommentdemo.representation.PostModelCutAssembler;
import com.wz.postcommentdemo.representation.PostModelAssembler;
import com.wz.postcommentdemo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    private final PostModelCutAssembler postModelCutAssembler;

    private final PostModelAssembler postModelAssembler;

    private final PagedResourcesAssembler<Post> pagedResourcesAssembler;

    @Autowired
    public PostController(PostService postService,
                          PostModelCutAssembler postModelCutAssembler,
                          PostModelAssembler postModelAssembler,
                          PagedResourcesAssembler<Post> pagedResourcesAssembler) {
        this.postService = postService;
        this.postModelAssembler = postModelAssembler;
        this.postModelCutAssembler = postModelCutAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{postId}")
    public PostModel getPost(@NotNull @PathVariable("postId") Long postId){
        return postService.getPostById(postId)
                .map(postModelAssembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException("Post was not found")
                );
    }

    @GetMapping
    public PagedModel<PostModelCut> getPosts(@PageableDefault(page = 0, size = 8) Pageable page){
        return pagedResourcesAssembler.toModel(postService.getPostPage(page), postModelCutAssembler);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostModel createPost(@RequestPart("post") @Valid Post post,
                                @RequestPart("image") @NotNull MultipartFile image){
        return postModelAssembler.toModel(postService.createNewPost(post, image));
    }

    @PutMapping("/{postId}")
    public PostModel editPost(@NotNull @PathVariable("postId") Long postId,
                              @NotNull @Valid @RequestPart Post post,
                              @RequestPart(value = "image", required = false) MultipartFile image){
        return postModelAssembler.toModel(postService.editPost(postId, post, image));
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@NotNull @PathVariable("postId") Long postId){
        postService.deletePostById(postId);
    }



}
