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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
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

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return 0L;
    }

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
     * 获取帖子
     */
    @GetMapping("/page/{page}")
    public Result<List<Post>> getPosts(@PathVariable Long page) {
        List<Post> posts = postService.getPosts(page);
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
     * 获取用户发布的帖子
     */
    @GetMapping("/{userId}/user")
    public Result<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return Result.success(posts);
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
     * 获取帖子的媒体
     */
    @GetMapping("/{id}/media")
    public Result<List<Media>> getMediaByPostId(@PathVariable Long id) {
        List<Media> media = mediaService.getMediaByPostId(id);
        return Result.success(media);
    }


    /**
     * 获取单个帖子的媒体
     */
    @GetMapping("/{id}/media/one")
    public Result<List<Media>> getOneMediaByPostId(@PathVariable Long id) {
        Media media = mediaService.getOneMediaByPostId(id);
        if (media == null) return Result.success(Collections.emptyList());;
        return Result.success(List.of(media));
    }


    /**
     * 点赞/取消点赞帖子
     */
    @PostMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<String> likePost(@PathVariable Long id) {
        try {
            boolean liked = postLikeService.likePost(id, getCurrentUserId());
            return Result.success(liked ? "点赞成功" : "取消点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传图片
     */
    @PostMapping("/{id}/upload")
    public Result<Media> uploadImage(@PathVariable Long id, @RequestBody MultipartFile file) {
        try {
            Media media = mediaService.uploadImage(file,id);
            return Result.success(media);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 搜索帖子
     * 
     * @param keyword 搜索关键词
     * @param page 页码，默认1
     * @param size 每页大小，默认10
     * @return 符合条件的帖子列表
     */
    @GetMapping("/search")
    public Result<List<Post>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") Long page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        try {
            List<Post> posts = postService.searchPostsByKeyword(keyword, page, size);
            return Result.success(posts);
        } catch (Exception e) {
            return Result.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取图片内容
     * 
     * @param mediaId 媒体ID
     * @return 图片二进制内容
     */
    @GetMapping("/media/{mediaId}/content")
    public ResponseEntity<byte[]> getImageContent(@PathVariable Long mediaId) {
        try {
            Media media = mediaService.getMediaWithContent(mediaId);
            if (media == null) {
                return ResponseEntity.notFound().build();
            }
            
            if (media.getFileContent() == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 根据媒体类型设置正确的内容类型
            MediaType contentType;
            if (media.getType() == 1) { // 图片
                // 这里简化处理，实际应用中可能需要根据文件名后缀判断具体图片类型
                String fileName = media.getUrl().toLowerCase();
                if (fileName.endsWith(".png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (fileName.endsWith(".gif")) {
                    contentType = MediaType.IMAGE_GIF;
                } else {
                    contentType = MediaType.IMAGE_JPEG;
                }
            } else if (media.getType() == 2) { // 视频
                contentType = MediaType.valueOf("video/mp4"); // 假设视频都是MP4格式
            } else {
                contentType = MediaType.APPLICATION_OCTET_STREAM; // 默认二进制流
            }
            
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(media.getFileContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 