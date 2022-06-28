package com.wz.postcommentdemo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wz.postcommentdemo.entity.Comment;
import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.repository.PostRepository;
import com.wz.postcommentdemo.util.FileUpload;
import com.wz.postcommentdemo.util.ImageProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@SpringBootApplication
public class PostCommentDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostCommentDemoApplication.class, args);
	}

}
