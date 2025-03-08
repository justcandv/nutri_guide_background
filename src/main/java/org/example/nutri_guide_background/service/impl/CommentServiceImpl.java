package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.CommentCreateDTO;
import org.example.nutri_guide_background.entity.Comment;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.mapper.CommentMapper;
import org.example.nutri_guide_background.mapper.PostMapper;
import org.example.nutri_guide_background.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public Comment addComment(Long postId, CommentCreateDTO dto) {
        // 查询帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getIsDeleted() == 1) {
            throw new RuntimeException("帖子不存在或已删除");
        }

        // 创建评论
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(dto.getUserId());
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId());
        comment.setCreateTime(LocalDateTime.now());
        
        // 保存评论
        save(comment);
        
        // 更新帖子评论数
        post.setCommentCount(post.getCommentCount() + 1);
        postMapper.updateById(post);
        
        return comment;
    }

    @Override
    public Comment getCommentById(Long id) {
        return getById(id);
    }
    
    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        // 查询帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getIsDeleted() == 1) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId)
               .orderByAsc(Comment::getParentId)  // 先按父评论ID排序
               .orderByDesc(Comment::getCreateTime);  // 再按创建时间降序排序
        
        // 查询评论列表
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            return false;
        }
        
        // 查询帖子
        Post post = postMapper.selectById(comment.getPostId());
        if (post != null) {
            // 更新帖子评论数
            post.setCommentCount(post.getCommentCount() - 1);
            postMapper.updateById(post);
        }
        
        // 删除评论
        return removeById(id);
    }
} 