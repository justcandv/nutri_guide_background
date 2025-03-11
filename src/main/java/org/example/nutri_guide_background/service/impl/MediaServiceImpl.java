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

import java.time.LocalDateTime;
import java.util.List;

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
} 