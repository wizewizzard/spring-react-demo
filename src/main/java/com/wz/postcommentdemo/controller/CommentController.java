package com.wz.postcommentdemo.controller;

import com.wz.postcommentdemo.entity.Comment;
import com.wz.postcommentdemo.representation.CommentModelAssembler;
import com.wz.postcommentdemo.representation.dto.CommentModel;
import com.wz.postcommentdemo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {

    private final CommentService commentService;

    private final CommentModelAssembler commentModelAssembler;

    private final PagedResourcesAssembler<Comment> pagedResourcesAssembler;

    @Autowired
    public CommentController(CommentService commentService,
                             CommentModelAssembler commentModelAssembler,
                             PagedResourcesAssembler<Comment> pagedResourcesAssembler) {
        this.commentService = commentService;
        this.commentModelAssembler = commentModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(value = "/{postId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommentModel addComment(@RequestBody @NotNull  @Valid Comment comment,
                              @PathVariable("postId") @NotNull Long postId) {

        return commentModelAssembler.toModel(commentService.addComment(postId, comment));
    }

    @GetMapping("/{postId}/comments")
    public PagedModel<CommentModel> getComments(@PathVariable("postId") @NotNull Long postId,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return pagedResourcesAssembler.toModel(commentService.getComments(postId, pageable), commentModelAssembler);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public void deleteComment(@PathVariable("postId") @NotNull Long postId,
                                                  @PathVariable("commentId") @NotNull Long commentId
                                                  ) {
        commentService.deleteComment(postId, commentId);
    }
}
