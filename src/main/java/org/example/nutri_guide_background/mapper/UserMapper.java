package org.example.nutri_guide_background.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.nutri_guide_background.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 可以添加自定义的SQL方法
} 