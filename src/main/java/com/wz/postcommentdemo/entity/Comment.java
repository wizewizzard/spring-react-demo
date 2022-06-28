package com.wz.postcommentdemo.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentIdSequence")
    @SequenceGenerator(name = "commentIdSequence", sequenceName = "comment_id_sequence")
    @Column(name = "comment_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "Author of the comment must be specified")
    @Column(nullable = false)
    private String author;

    @Getter
    @Setter
    @NotBlank(message = "Body of the comment must not be blank")
    @Column(nullable = false)
    private String content;

    @Getter
    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Getter
    @Setter
    @ToString.Exclude
    private Post post;

    public void setPublishedAt(LocalDateTime publishedAt){
        this.publishedAt = publishedAt;
    }
}
