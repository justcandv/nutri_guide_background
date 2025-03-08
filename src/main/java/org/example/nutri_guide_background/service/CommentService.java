package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.dto.CommentCreateDTO;
import org.example.nutri_guide_background.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {
    
    /**
     * 添加评论
     *
     * @param postId 帖子ID
     * @param dto 评论DTO
     * @return 创建的评论
     */
    Comment addComment(Long postId, CommentCreateDTO dto);
    
    /**
     * 根据ID获取评论
     *
     * @param id 评论ID
     * @return 评论信息
     */
    Comment getCommentById(Long id);
    
    /**
     * 根据帖子ID获取所有评论
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<Comment> getCommentsByPostId(Long postId);
    
    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 是否删除成功
     */
    boolean deleteComment(Long id);
} 