package com.wz.postcommentdemo.util;

import com.wz.postcommentdemo.exception.exception.FileException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
public class FileUpload {

    public void saveFile(String directory, String fileName, Consumer<File> fileSave) throws FileException {
        Path path = Paths.get( directory);
        try {
            if (!Files.exists(path))
                Files.createDirectories(path);
            fileSave.accept(path.resolve(fileName).toFile());
        } catch (IOException e) {
            throw new FileException("Unable to prepare file structure for a file in " + directory, e);
        }
    }

    public void deleteFiles(String directory, String... fileNames) throws FileException {
        Path path = Paths.get(directory);
        try {
        for(String fileName : fileNames){
            Path filePath = path.resolve(fileName);

                if (Files.exists(filePath) && Files.isRegularFile(filePath))
                    Files.delete(filePath);

        }
        } catch (IOException e) {
            throw new FileException("Unable to delete files from " + directory, e);
        }
    }

    public void deleteDirectoryWithFiles(String directory) throws FileException {
        Path root = Paths.get(directory);
        try (Stream<Path> walk = Files.walk(root)){
            walk.forEach(file -> {
                try {
                    if(Files.isRegularFile(file))
                        Files.delete(file);
                } catch (IOException e) {
                    throw new RuntimeException("Could not delete a directory " + directory, e);
                }
            });
            Files.delete(root);
        } catch (IOException e) {
            throw new FileException("Could not delete a directory " + directory, e);
        }
    }
}
