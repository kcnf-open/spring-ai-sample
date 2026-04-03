package com.kcnf.ai.controller;


import com.kcnf.ai.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 简单生成图片，返回图片 URL
     * GET /api/image/url?prompt=一只可爱的小猫
     */
    @GetMapping("/url")
    public Map<String, String> generateImageUrl(@RequestParam String prompt) {
        String imageUrl = imageService.generateImageUrl(prompt);
        return Map.of("prompt", prompt, "imageUrl", imageUrl);
    }

    /**
     * 自定义参数生成图片，返回图片 URL
     * GET /api/image/url/options?prompt=日落海滩&width=1024&height=768&style=vivid
     */
    @GetMapping("/url/options")
    public Map<String, String> generateImageWithOptions(
            @RequestParam String prompt,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height,
            @RequestParam(required = false) String style) {
        String imageUrl = imageService.generateImageUrl(prompt, width, height, style);
        return Map.of("prompt", prompt, "imageUrl", imageUrl);
    }

    /**
     * 生成图片并直接在浏览器中显示（推荐）
     * GET /api/image/display?prompt=一座雪山
     */
    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("prompt") String prompt) {
        try {
            byte[] imageBytes = imageService.generateImageBytes(prompt);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}