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
        
        // 设置文件内容（如果有）
        if (dto.getFileContent() != null) {
            media.setFileContent(dto.getFileContent());
        }
        
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
        try {
            // 查询帖子是否存在
            Post post = postMapper.selectById(postId);
            if (post == null || post.getIsDeleted() == 1) {
                throw new RuntimeException("帖子不存在或已删除");
            }

            // 生成唯一文件名
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            
            // 创建媒体记录
            Media media = new Media();
            media.setPostId(postId);
            
            // 生成完整的访问URL路径，而不仅仅是文件名
            // 使用相对路径 "/posts/media/{id}/content" 的格式
            // 注意：此处先保存一个临时值，在save后获取到media的id再更新
            media.setUrl(fileName); // 临时设置为文件名
            media.setType(1); // 1-图片
            media.setCreateTime(LocalDateTime.now());
            
            // 存储文件内容到数据库
            try {
                media.setFileContent(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("文件读取失败", e);
            }
            
            // 保存媒体记录
            save(media);
            
            // 获取到ID后更新URL为完整的访问路径
            // 使用ServletUriComponentsBuilder生成完整的URL（包含域名）
            String accessUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/posts/media/")
                    .path(media.getId().toString())
                    .path("/content")
                    .toUriString();
            media.setUrl(accessUrl);
            
            // 更新媒体记录
            updateById(media);
            
            return media;
        } catch (Exception e) {
            throw new RuntimeException("上传图片失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Media getMediaWithContent(Long mediaId) {
        Media media = getById(mediaId);
        
        if (media != null && media.getFileContent() == null) {
            // 如果没有获取到文件内容，尝试从数据库重新加载
            // 这里使用Mapper直接查询可以避免被JsonIgnore影响
            media = baseMapper.selectById(mediaId);
        }
        
        return media;
    }

} 