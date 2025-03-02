package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.PostCreateDTO;
import org.example.nutri_guide_background.dto.PostUpdateDTO;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.mapper.PostMapper;
import org.example.nutri_guide_background.service.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子服务实现类
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    
    @Override
    public Post createPost(PostCreateDTO dto) {
        Post post = new Post();
        post.setUserId(dto.getUserId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setIsDeleted(0);
        post.setCreateTime(LocalDateTime.now());
        
        save(post);
        return post;
    }
    
    @Override
    public List<Post> getAllPosts() {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getIsDeleted, 0)
               .orderByDesc(Post::getCreateTime);
        return list(wrapper);
    }
    
    @Override
    public Post getPostById(Long id) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getId, id)
               .eq(Post::getIsDeleted, 0);
        return getOne(wrapper);
    }
    
    @Override
    public boolean updatePost(Long id, PostUpdateDTO dto) {
        Post post = getById(id);
        if (post == null || post.getIsDeleted() == 1) {
            return false;
        }
        
        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        
        if (dto.getIsDeleted() != null) {
            post.setIsDeleted(dto.getIsDeleted() ? 1 : 0);
        }
        
        return updateById(post);
    }
    
    @Override
    public boolean deletePost(Long id) {
        Post post = getById(id);
        if (post == null || post.getIsDeleted() == 1) {
            return false;
        }
        
        post.setIsDeleted(1);
        return updateById(post);
    }
}