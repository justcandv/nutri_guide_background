package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.dto.PostLikeDTO;
import org.example.nutri_guide_background.entity.PostLike;

/**
 * 帖子点赞服务接口
 */
public interface PostLikeService extends IService<PostLike> {
    
    /**
     * 点赞帖子
     *
     * @param postId 帖子ID
     * @return 是否点赞成功
     */
    boolean likePost(Long postId, Long userId);
} 