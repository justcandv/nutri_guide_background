package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.dto.PostCreateDTO;
import org.example.nutri_guide_background.dto.PostUpdateDTO;
import org.example.nutri_guide_background.entity.Post;

import java.util.List;

/**
 * 帖子服务接口
 */
public interface PostService extends IService<Post> {
    
    /**
     * 创建帖子
     *
     * @param dto 创建帖子DTO
     * @return 创建的帖子信息
     */
    Post createPost(PostCreateDTO dto);
    
    /**
     * 获取所有帖子
     *
     * @return 帖子列表
     */
    List<Post> getAllPosts();
    
    /**
     * 根据ID获取帖子
     *
     * @param id 帖子ID
     * @return 帖子信息
     */
    Post getPostById(Long id);
    
    /**
     * 更新帖子
     *
     * @param id 帖子ID
     * @param dto 更新帖子DTO
     * @return 是否更新成功
     */
    boolean updatePost(Long id, PostUpdateDTO dto);
    
    /**
     * 删除帖子
     *
     * @param id 帖子ID
     * @return 是否删除成功
     */
    boolean deletePost(Long id);
} 