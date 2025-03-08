package org.example.nutri_guide_background.controller;

import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.entity.Comment;
import org.example.nutri_guide_background.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    /**
     * 获取单个评论
     */
    @GetMapping("/{id}")
    public Result<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        return Result.success(comment);
    }
    
    /**
     * 获取帖子下的所有评论
     */
    @GetMapping("/post/{postId}")
    public Result<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        boolean deleted = commentService.deleteComment(id);
        if (!deleted) {
            return Result.error("评论不存在或删除失败");
        }
        return Result.success("删除成功");
    }
} 