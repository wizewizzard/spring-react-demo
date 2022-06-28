package com.wz.postcommentdemo.representation;

import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.representation.dto.PostModel;
import com.wz.postcommentdemo.util.UploadsProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;

@ExtendWith(MockitoExtension.class)
class CommentModelAssemblerTest {

    @InjectMocks
    private PostModelAssembler underTest;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private UploadsProperties uploadsProperties;

    @Test
    public void testToCutModelConversion(){
        String uploadPath = "uploads/images";
        Mockito.when(uploadsProperties.getImageUploads().getPath()).thenReturn(uploadPath);
        Post post = new Post();
        post.setId(223L);
        post.setTitle("Test title");
        post.setBrief("Test brief");
        post.setContent("Test content");
        post.setImage("image.jpg");
        post.setPostedAt(LocalDateTime.parse("2022-05-22T13:56"));

        PostModel modelCut = underTest.toModel(post);

        assertThat(modelCut).isNotNull();
        assertThat(modelCut.getId()).isEqualTo(223L);
        assertThat(modelCut.getTitle()).isEqualTo(post.getTitle());
        assertThat(modelCut.getBrief()).isEqualTo(post.getBrief());
        assertThat(modelCut.getContent()).isEqualTo(post.getContent());
        assertThat(modelCut.getImagePath()).isEqualTo(uploadPath + "/" + post.getId() + "/" + post.getImage());
    }

}