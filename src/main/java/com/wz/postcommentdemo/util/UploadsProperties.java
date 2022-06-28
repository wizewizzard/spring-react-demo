package com.wz.postcommentdemo.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "uploads")
@Getter
@Setter
public class UploadsProperties {

    private ImageUploads imageUploads;

    @Getter
    @Setter
    public static class ImageUploads {

        private int thumbsSize;

        private String uploadDir;

        private String path;

    }
}
