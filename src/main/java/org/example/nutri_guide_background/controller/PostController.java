package org.example.nutri_guide_background.controller;

import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CommentCreateDTO;
import org.example.nutri_guide_background.dto.MediaCreateDTO;
import org.example.nutri_guide_background.dto.PostCreateDTO;
import org.example.nutri_guide_background.dto.PostLikeDTO;
import org.example.nutri_guide_background.dto.PostUpdateDTO;
import org.example.nutri_guide_background.entity.Comment;
import org.example.nutri_guide_background.entity.Media;
import org.example.nutri_guide_background.entity.Post;
import org.example.nutri_guide_background.service.CommentService;
import org.example.nutri_guide_background.service.MediaService;
import org.example.nutri_guide_background.service.PostLikeService;
import org.example.nutri_guide_background.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 帖子控制器
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private MediaService mediaService;
    
    @Autowired
    private PostLikeService postLikeService;
    
    /**
     * 创建帖子
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Post> createPost(@Valid @RequestBody PostCreateDTO dto) {
        try {
            Post post = postService.createPost(dto);
            return Result.success(post);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有帖子
     */
    @GetMapping
    public Result<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return Result.success(posts);
    }
    
    /**
     * 获取单个帖子
     */
    @GetMapping("/{id}")
    public Result<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return Result.error("帖子不存在或已删除");
        }
        return Result.success(post);
    }
    
    /**
     * 更新帖子
     */
    @PutMapping("/{id}")
    public Result<String> updatePost(@PathVariable Long id, @RequestBody PostUpdateDTO dto) {
        boolean updated = postService.updatePost(id, dto);
        if (!updated) {
            return Result.error("帖子不存在或已删除");
        }
        return Result.success("更新成功");
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result<String> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        if (!deleted) {
            return Result.error("帖子不存在或已删除");
        }
        return Result.success("删除成功");
    }
    
    /**
     * 添加评论
     */
    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Comment> addComment(@PathVariable Long id, @Valid @RequestBody CommentCreateDTO dto) {
        try {
            Comment comment = commentService.addComment(id, dto);
            return Result.success(comment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 添加媒体
     */
    @PostMapping("/{id}/media")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Media> addMedia(@PathVariable Long id, @Valid @RequestBody MediaCreateDTO dto) {
        try {
            Media media = mediaService.addMedia(id, dto);
            return Result.success(media);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 点赞/取消点赞帖子
     */
    @PostMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<String> likePost(@PathVariable Long id, @Valid @RequestBody PostLikeDTO dto) {
        try {
            boolean liked = postLikeService.likePost(id, dto);
            return Result.success(liked ? "点赞成功" : "取消点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 