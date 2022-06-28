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
@Relation(collectionRelation = "posts", itemRelation = "post")
public class PostModel extends RepresentationModel<PostModelCut> {

    private Long id;

    private String title;

    private String brief;

    private String content;

    private String imagePath;

    private LocalDateTime postedAt;

}