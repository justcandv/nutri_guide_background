package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.MediaCreateDTO;
import org.example.nutri_guide_background.entity.Media;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.mapper.MediaMapper;
import org.example.nutri_guide_background.mapper.PostMapper;
import org.example.nutri_guide_background.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 媒体服务实现类
 */
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public Media addMedia(Long postId, MediaCreateDTO dto) {
        // 查询帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getIsDeleted() == 1) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        
        // 创建媒体记录
        Media media = new Media();
        media.setPostId(postId);
        media.setUrl(dto.getUrl());
        media.setType(dto.getType());
        media.setCreateTime(LocalDateTime.now());
        
        // 保存媒体记录
        save(media);
        
        return media;
    }

    @Override
    public List<Media> getMediaByPostId(Long id) {
        return lambdaQuery()
                .eq(Media::getPostId, id)
                .list();
    }


    @Override
    public Media uploadImage(MultipartFile file, Long postId) {
        String uploadPath = "src/main/resources/uploads";

        // 生成唯一文件名
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            // 创建存储目录（如果不存在）
            Files.createDirectories(filePath.getParent());
            // 保存文件
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(uploadPath)
                .path(fileName)
                .toUriString();

        MediaCreateDTO dto = new MediaCreateDTO();
        dto.setUrl(fileUrl);
        dto.setType(1);

        // 查询帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getIsDeleted() == 1) {
            throw new RuntimeException("帖子不存在或已删除");
        }

        // 创建媒体记录
        Media media = new Media();
        media.setPostId(postId);
        media.setUrl(dto.getUrl());
        media.setType(dto.getType());
        media.setCreateTime(LocalDateTime.now());

        // 保存媒体记录
        save(media);
        return media;
    }

} 