package com.wz.postcommentdemo.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "postIdSequence", sequenceName = "post_id_sequence")
    @Column(name = "post_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    @NotBlank(message = "Title must not be blank")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Getter
    @Setter
    @Column(length = 512)
    @NotBlank(message = "Brief must not be blank")
    @Size(min = 8, message = "Brief must be at least 8 characters long")
    private String brief;

    @Getter
    @Setter
    @Column(length = 1024 * 16)
    @NotBlank(message = "Content must not be blank")
    @Size(min = 8, message = "Content must be at least 8 characters long")
    private String content;

    @Getter
    @Setter
    @Column(name = "image_path")
    private String image;

    @Getter
    @Setter
    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setPost(this);
    }

    @Transient
    public String getImagePath(){
        return this.id + "/" + this.getImage();
    }

    /*@PrePersist
    public void setPostDateTime(){
        this.postedAt = LocalDateTime.now();
    }*/


}
