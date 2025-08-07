package com.saas.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件服务控制器
 * 
 * @author SaaS Xbox Team
 */
@Tag(name = "文件服务", description = "文件上传下载相关接口")
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload.path:/tmp/uploads/}")
    private String uploadPath;

    /**
     * 获取上传的文件
     */
    @Operation(summary = "获取上传的文件", description = "根据文件路径获取上传的文件")
    @GetMapping("/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest request) {
        try {
            // 获取请求路径中的文件路径
            String requestUri = request.getRequestURI();
            String filePath = requestUri.substring(requestUri.indexOf("/files/") + 7);
            
            Path file = Paths.get(uploadPath).resolve(filePath);
            File actualFile = file.toFile();
            
            if (!actualFile.exists() || !actualFile.isFile()) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(actualFile);
            
            // 获取文件的 MIME 类型
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
                    .body(resource);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}