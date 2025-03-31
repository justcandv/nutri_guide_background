package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.PostCreateDTO;
import org.example.nutri_guide_background.dto.PostUpdateDTO;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.entity.User;
import org.example.nutri_guide_background.mapper.PostMapper;
import org.example.nutri_guide_background.mapper.UserMapper;
import org.example.nutri_guide_background.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 帖子服务实现类
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private UserMapper userMapper;

    // 填充用户信息的辅助方法
    private void fillUserInfo(Post post) {
        if (post != null && post.getUserId() != null) {
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                post.setAvatarUrl(user.getAvatarUrl());
                post.setUserName(user.getUsername());
            }
            System.out.println("User : " + post.getUserName());
        }
    }

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
        List<Post> posts = list(wrapper);
        posts.forEach(this::fillUserInfo);
        return posts;
    }

    @Override
    public List<Post> getPosts(Long page) {
        // 设置分页参数（页码从1开始）
        Page<Post> pageParam = new Page<>(page, 10); // 每页10条

        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getIsDeleted, 0)
                .orderByDesc(Post::getCreateTime);

        // 执行分页查询
        Page<Post> postPage = page(pageParam, wrapper);
        List<Post> posts = postPage.getRecords();
        posts.forEach(this::fillUserInfo);
        return posts;
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
            LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Post::getUserId, userId)
                   .eq(Post::getIsDeleted, 0)
                   .orderByDesc(Post::getCreateTime);
            return list(wrapper);
    }

    @Override
    public Post getPostById(Long id) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getId, id)
               .eq(Post::getIsDeleted, 0);
        Post post = getOne(wrapper);
        fillUserInfo(post);
        return post;
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
    
    @Override
    public List<Post> searchPostsByKeyword(String keyword, Long page, Integer size) {
        // 默认值处理
        long currentPage = (page == null || page < 1) ? 1 : page;
        int pageSize = (size == null || size < 1) ? 10 : size;
        
        // 创建分页对象
        Page<Post> pageParam = new Page<>(currentPage, pageSize);
        
        // 构建查询条件
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getIsDeleted, 0)
               .and(keyword != null && !keyword.trim().isEmpty(), 
                    w -> w.like(Post::getTitle, keyword)
                          .or()
                          .like(Post::getContent, keyword)
               )
               .orderByDesc(Post::getLikeCount);
        
        // 执行分页查询
        Page<Post> postPage = page(pageParam, wrapper);
        List<Post> posts = postPage.getRecords();
        
        // 填充用户信息
        posts.forEach(this::fillUserInfo);
        
        return posts;
    }
}