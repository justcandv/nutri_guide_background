package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.PostLikeDTO;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.entity.PostLike;
import org.example.nutri_guide_background.mapper.PostLikeMapper;
import org.example.nutri_guide_background.mapper.PostMapper;
import org.example.nutri_guide_background.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 帖子点赞服务实现类
 */
@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public boolean likePost(Long postId, PostLikeDTO dto) {
        // 查询帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getIsDeleted() == 1) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        
        // 查询是否已点赞
        LambdaQueryWrapper<PostLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostLike::getPostId, postId)
               .eq(PostLike::getUserId, dto.getUserId());
        
        PostLike existingLike = getOne(wrapper, false);
        
        // 如果已点赞，则取消点赞
        if (existingLike != null) {
            // 更新帖子点赞数
            post.setLikeCount(post.getLikeCount() - 1);
            postMapper.updateById(post);
            
            // 删除点赞记录
            return remove(wrapper);
        } else {
            // 创建点赞记录
            PostLike postLike = new PostLike();
            postLike.setPostId(postId);
            postLike.setUserId(dto.getUserId());
            postLike.setCreateTime(LocalDateTime.now());
            
            // 更新帖子点赞数
            post.setLikeCount(post.getLikeCount() + 1);
            postMapper.updateById(post);
            
            // 保存点赞记录
            return save(postLike);
        }
    }
} 