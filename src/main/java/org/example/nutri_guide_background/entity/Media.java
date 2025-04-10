package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体实体类
 */
@Data
@TableName("media")
public class Media {
    
    /**
     * 媒体ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 文件名称或访问路径
     */
    private String url;
    
    /**
     * 媒体类型（1-图片 2-视频）
     */
    private Integer type;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 文件二进制内容
     * 使用@JsonIgnore注解防止在一般接口中被序列化
     */
    @JsonIgnore
    private byte[] fileContent;
} 