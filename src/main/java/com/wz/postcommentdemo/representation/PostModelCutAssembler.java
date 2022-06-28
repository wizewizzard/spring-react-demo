package com.wz.postcommentdemo.representation;

import com.wz.postcommentdemo.controller.PostController;
import com.wz.postcommentdemo.representation.dto.PostModelCut;
import com.wz.postcommentdemo.entity.Post;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.wz.postcommentdemo.util.UploadsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class PostModelCutAssembler extends RepresentationModelAssemblerSupport<Post, PostModelCut> {

    private final UploadsProperties uploadsProperties;

    @Autowired
    public PostModelCutAssembler(UploadsProperties uploadsProperties) {
        super(PostController.class, PostModelCut.class);
        this.uploadsProperties = uploadsProperties;
    }

    @Override
    public PostModelCut toModel(Post postEntity) {
        PostModelCut postModelCut = instantiateModel(postEntity);
        postModelCut.add(
                linkTo(
                        methodOn(PostController.class).getPost(postEntity.getId())
                )
                    .withSelfRel());
        postModelCut.setId(postEntity.getId());
        postModelCut.setTitle(postEntity.getTitle());
        postModelCut.setPostedAt(postEntity.getPostedAt());
        postModelCut.setImagePath(uploadsProperties.getImageUploads().getPath() + "/" + postEntity.getId() + "/thumbs/" + postEntity.getImage());
        postModelCut.setBrief(postEntity.getBrief());
        return postModelCut;
    }


}
