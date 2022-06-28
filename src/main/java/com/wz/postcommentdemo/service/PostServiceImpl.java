package com.wz.postcommentdemo.service;

import com.wz.postcommentdemo.entity.Post;
import com.wz.postcommentdemo.exception.exception.ResourceNotFoundException;
import com.wz.postcommentdemo.exception.exception.FileException;
import com.wz.postcommentdemo.repository.PostRepository;
import com.wz.postcommentdemo.util.FileUpload;
import com.wz.postcommentdemo.util.ImageProcess;
import com.wz.postcommentdemo.util.UploadsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    private final UploadsProperties uploadsProperties;
    private final PostRepository postRepository;

    private final FileUpload fileUploader;

    private final ImageProcess imageProcess;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           UploadsProperties uploadsProperties,
                           FileUpload fileUploader,
                           ImageProcess imageProcess) {
        this.postRepository = postRepository;
        this.fileUploader = fileUploader;
        this.imageProcess = imageProcess;
        this.uploadsProperties = uploadsProperties;
    }

    @Override
    @Transactional
    public Post createNewPost(Post post,
                                   MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        post.setImage(originalFilename);
        post.setPostedAt(LocalDateTime.now());
        Post saved = postRepository.save(post);
        try{
            String uploadDirectory = uploadsProperties.getImageUploads().getUploadDir();
            BufferedImage orig = ImageIO.read(image.getInputStream());
            BufferedImage thumb = imageProcess.scaleImage(orig, uploadsProperties.getImageUploads().getThumbsSize());
            saveImage(uploadDirectory + "/" + saved.getId(), originalFilename, orig);
            saveImage(uploadDirectory + "/" + saved.getId() + "/thumbs", originalFilename, thumb);
        }
        catch(IOException | FileException exc){
            log.error("File was not saved. ", exc);
            throw new FileException(exc.getMessage(), exc);
        }

        return saved;
    }

    @Transactional
    @Override
    public Post editPost(Long postId, Post editedPost, MultipartFile image) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            post.setTitle(editedPost.getTitle());
            post.setBrief(editedPost.getBrief());
            post.setContent(editedPost.getContent());
            if(image != null && !image.isEmpty()){
                try {
                    String uploadDirectory = uploadsProperties.getImageUploads().getUploadDir();
                    fileUploader.deleteFiles(uploadDirectory + "/" + post.getId(), post.getImage());
                    fileUploader.deleteFiles(uploadDirectory + "/" + post.getId() + "/thumbs", post.getImage());
                    BufferedImage orig = ImageIO.read(image.getInputStream());
                    BufferedImage thumb = imageProcess.scaleImage(orig, uploadsProperties.getImageUploads().getThumbsSize());
                    saveImage(uploadDirectory + "/" + post.getId(), image.getOriginalFilename(), orig);
                    saveImage(uploadDirectory + "/" + post.getId() + "/thumbs", image.getOriginalFilename(), thumb);
                } catch (IOException | FileException exc) {
                    log.error("Could not delete image when updating the post ", exc);
                    throw new FileException(exc.getMessage(), exc);
                }
                post.setImage(image.getOriginalFilename());
            }
            return postRepository.save(post);
        }
        else{
            throw new ResourceNotFoundException("Post with this id was not found");
        }
    }

    @Override
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId)
                .or(Optional::empty);
    }

    @Override
    public Page<Post> getPostPage(Pageable p) {
        return postRepository.findAll(p);
    }

    @Transactional
    @Override
    public void deletePostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            try {
                String uploadDirectory = uploadsProperties.getImageUploads().getUploadDir();
                fileUploader.deleteDirectoryWithFiles(uploadDirectory + "/" + post.getId() + "/thumbs");
                fileUploader.deleteDirectoryWithFiles(uploadDirectory + "/" + post.getId());
            } catch (FileException exc) {
                log.error("Directory can not be deleted for the post. ", exc);
                throw new FileException(exc.getMessage(), exc);
            }
            postRepository.delete(post);
        }
        else{
            throw new ResourceNotFoundException("Post with this id was not found");
        }
    }

    private void saveImage(String directory, String filename, BufferedImage image){
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
