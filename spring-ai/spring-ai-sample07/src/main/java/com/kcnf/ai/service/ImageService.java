package com.kcnf.ai.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@Service
public class ImageService {

    @Autowired
    private ImageModel imageModel;

    /**
     * 生成图片，返回图片 URL
     */
    public String generateImageUrl(String prompt) {
        ImageResponse response = imageModel.call(new ImagePrompt(prompt));
        return response.getResult().getOutput().getUrl();
    }

    /**
     * 生成图片，返回图片 URL（带自定义参数）
     */
    public String generateImageUrl(String prompt, Integer width, Integer height, String style) {
        var options = ImageOptionsBuilder.builder()
                .model("CogView-3-Flash")
                .width(width != null ? width : 1024)
                .height(height != null ? height : 1024)
                .responseFormat("png")
                .style(style != null ? style : "natural")  // vivid: 生动风格, natural: 自然风格
                .build();
        ImageResponse response = imageModel.call(new ImagePrompt(prompt, options));
        return response.getResult().getOutput().getUrl();
    }

    /**
     * 生成图片，返回图片二进制数据（可直接写入 HTTP 响应）
     */
    public byte[] generateImageBytes(String prompt) throws IOException {
        String imageUrl = generateImageUrl(prompt);
        return downloadImage(imageUrl);
    }

    /**
     * 从 URL 下载图片
     */
    private byte[] downloadImage(String imageUrl) throws IOException {
        URL url = URI.create(imageUrl).toURL();
        try (InputStream in = url.openStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        }
    }
}