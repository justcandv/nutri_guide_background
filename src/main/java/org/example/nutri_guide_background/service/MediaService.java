package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.dto.MediaCreateDTO;
import org.example.nutri_guide_background.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 媒体服务接口
 */
public interface MediaService extends IService<Media> {
    
    /**
     * 添加媒体
     *
     * @param postId 帖子ID
     * @param dto 媒体DTO
     * @return 创建的媒体
     */
    Media addMedia(Long postId, MediaCreateDTO dto);

    List<Media> getMediaByPostId(Long id);

    Media uploadImage(MultipartFile file, Long postId);
}