package com.wz.postcommentdemo.util;

import com.wz.postcommentdemo.exception.exception.FileException;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageProcess {

    private FileUpload fileUpload;

    public ImageProcess(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public BufferedImage scaleImage(BufferedImage image, int targetSize){
        return Scalr.resize(image, targetSize);
    }

    public void saveImage(String directory, String filename, BufferedImage image){
        fileUpload.saveFile(
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
