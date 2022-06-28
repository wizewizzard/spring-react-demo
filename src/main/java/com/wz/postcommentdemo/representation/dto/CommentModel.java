package com.wz.postcommentdemo.representation.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "comments", itemRelation = "comment")
public class CommentModel extends RepresentationModel<CommentModel> {
    private Long id;

    private String content;

    private String author;

    private LocalDateTime publishedAt;
}
