package com.wz.postcommentdemo.representation;

import com.wz.postcommentdemo.controller.PostController;
import com.wz.postcommentdemo.representation.dto.PostModel;
import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.util.UploadsProperties;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostModelAssembler extends RepresentationModelAssemblerSupport<Post, PostModel> {

    private final UploadsProperties uploadsProperties;

    public PostModelAssembler(UploadsProperties uploadsProperties) {
        super(PostController.class, PostModel.class);
        this.uploadsProperties = uploadsProperties;
    }

    @Override
    public PostModel toModel(Post postEntity) {
        PostModel postModel = instantiateModel(postEntity);
        postModel.add(linkTo(methodOn(PostController.class).getPost(postEntity.getId())).withSelfRel());
        postModel.add(linkTo(methodOn(PostController.class).getPosts(null)).withRel("posts"));
        postModel.setId(postEntity.getId());
        postModel.setTitle(postEntity.getTitle());
        postModel.setPostedAt(postEntity.getPostedAt());
        postModel.setImagePath(uploadsProperties.getImageUploads().getPath() + "/" + postEntity.getImagePath());
        postModel.setContent(postEntity.getContent());
        postModel.setBrief(postEntity.getBrief());
        return postModel;
    }
}
