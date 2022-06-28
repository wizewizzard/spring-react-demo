package com.wz.postcommentdemo.util;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageProcess {

    public BufferedImage scaleImage(BufferedImage image, int targetSize){
        return Scalr.resize(image, targetSize);
    }

}
