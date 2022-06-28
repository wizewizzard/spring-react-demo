package com.wz.postcommentdemo.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wz.postcommentdemo.entity.Comment;
import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.exception.exception.FileException;
import com.wz.postcommentdemo.repository.PostRepository;
import com.wz.postcommentdemo.util.FileUpload;
import com.wz.postcommentdemo.util.ImageProcess;
import com.wz.postcommentdemo.util.UploadsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Configuration
public class DemoDataConfig {

    @Bean
    public CommandLineRunner demoDataFiller(@Autowired UploadsProperties uploadsProperties,
                                                @Autowired PostRepository postRepository,
                                               @Autowired ImageProcess imageProcess,
                                               @Autowired FileUpload fileUpload,
                                               @Autowired ObjectMapper mapper){
        return args -> {
            Path path = Paths.get("stubs");
            try (Stream<Path> walk = Files.walk(path)) {
                walk.forEach(
                        file -> {
                            if(Files.isRegularFile(file)){
                                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                                    JsonNode jsonNode = mapper.readTree(reader);
                                    Post post = new Post();
                                    post.setTitle(jsonNode.get("title").textValue());
                                    post.setBrief(jsonNode.get("brief").textValue());
                                    post.setContent(jsonNode.get("content").textValue());
                                    post.setImage(jsonNode.get("image").textValue());
                                    post.setPostedAt(LocalDateTime.parse(jsonNode.get("postedAt").textValue(), DateTimeFormatter.ISO_DATE_TIME));
                                    JsonNode commentsNode = jsonNode.get("comments");
                                    if(commentsNode != null && commentsNode.isArray()){
                                        for(JsonNode commentNode: commentsNode){
                                            Comment comment = new Comment();
                                            comment.setAuthor(commentNode.get("author").textValue());
                                            comment.setContent(commentNode.get("content").textValue());
                                            comment.setPublishedAt(LocalDateTime.parse(commentNode.get("publishedAt").textValue(), DateTimeFormatter.ISO_DATE_TIME));
                                            post.addComment(comment);
                                        }
                                    }
                                    post = postRepository.save(post);
                                    BufferedImage orig = ImageIO.read(Files.newInputStream(path.resolve("images").resolve(post.getId().toString()).resolve(post.getImage())));
                                    BufferedImage thumb = imageProcess.scaleImage(orig, uploadsProperties.getImageUploads().getThumbsSize());
                                    saveImage(fileUpload, uploadsProperties.getImageUploads().getUploadDir() + "/" + post.getId(), post.getImage(), orig);
                                    saveImage(fileUpload, uploadsProperties.getImageUploads().getUploadDir() + "/" + post.getId() + "/thumbs", post.getImage(), thumb);

                                } catch (IOException e) {
                                    System.out.println(e);
                                }
                            }
                        }
                );
            }
            catch (IOException e){
                System.out.println(e);
            }
        };
    }

    private void saveImage(FileUpload fileUploader, String directory, String filename, BufferedImage image){
        fileUploader.saveFile(
                directory,
                filename,
                (File outputFile) -> {
                    try {
                        ImageIO.write(image, "jpg", outputFile);
                    } catch (IOException e) {
                        throw new FileException("Error while saving image to %s".formatted(outputFile.getPath()),e);
                    }
                });
    }
}
