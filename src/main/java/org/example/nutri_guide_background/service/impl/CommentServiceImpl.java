package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.CommentCreateDTO;
import org.example.nutri_guide_background.entity.Comment;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.entity.User;
import org.example.nutri_guide_background.mapper.CommentMapper;
import org.example.nutri_guide_background.mapper.PostMapper;
import org.example.nutri_guide_background.mapper.UserMapper;
import org.example.nutri_guide_background.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;

    // 填充评论的用户信息（用户名和头像）
    private Comment fillCommentUsername(Comment comment) {
        if (comment != null && comment.getUserId() != null) {
            User user = userMapper.selectById(comment.getUserId());
            if (user != null) {
                comment.setUsername(user.getUsername());
                comment.setAvatarUrl(user.getAvatarUrl());
            }
        }
        return comment;
    }
    
    // 批量填充评论列表的用户信息
    private List<Comment> fillCommentsUsername(List<Comment> comments) {
        return comments.stream()
                .map(this::fillCommentUsername)
                .collect(Collectors.toList());
    }

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
        
        // 填充用户信息并返回
        return fillCommentUsername(comment);
    }

    @Override
    public Comment getCommentById(Long id) {
        Comment comment = getById(id);
        return fillCommentUsername(comment);
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
        
        // 查询评论列表并填充用户信息
        List<Comment> comments = list(wrapper);
        return fillCommentsUsername(comments);
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