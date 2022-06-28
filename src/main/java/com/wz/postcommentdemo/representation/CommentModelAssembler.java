package com.wz.postcommentdemo.representation;

import com.wz.postcommentdemo.controller.CommentController;
import com.wz.postcommentdemo.controller.PostController;
import com.wz.postcommentdemo.entity.Comment;
import com.wz.postcommentdemo.representation.dto.CommentModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CommentModelAssembler extends RepresentationModelAssemblerSupport<Comment, CommentModel> {
    public CommentModelAssembler() {
        super(CommentController.class, CommentModel.class);
    }

    @Override
    public CommentModel toModel(Comment commentEntity) {
        CommentModel commentModel = instantiateModel(commentEntity);
        commentModel.setId(commentEntity.getId());
        commentModel.setAuthor(commentEntity.getAuthor());
        commentModel.setContent(commentEntity.getContent());
        commentModel.setPublishedAt(commentEntity.getPublishedAt());
        return commentModel;
    }
}
