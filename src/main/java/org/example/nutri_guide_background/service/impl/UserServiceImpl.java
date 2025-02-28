package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.UserRegisterDTO;
import org.example.nutri_guide_background.entity.User;

import lombok.extern.slf4j.Slf4j;
import org.example.nutri_guide_background.mapper.UserMapper;
import org.example.nutri_guide_background.service.UserService;
import org.example.nutri_guide_background.util.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Result<User> register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        if (userMapper.selectOne(queryWrapper) != null) {
            return Result.error("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userMapper.insert(user);
        
        user.setPassword(null); // 返回前清除密码
        return Result.success(user);
    }

    @Override
    public Result<Map<String, Object>> login(String username, String password) {
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        // 生成token
        String token = jwtUtils.generateToken(user.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        user.setPassword(null);
        map.put("user", user);
        
        return Result.success(map);
    }

    @Override
    public Result<User> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null); // 清除密码信息
        return Result.success(user);
    }

    @Override
    public Result<User> updateUser(Long id, User user) {
        // 检查用户是否存在
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        // 如果修改用户名，检查新用户名是否已存在
        if (!StringUtils.isEmpty(user.getUsername()) &&
                !user.getUsername().equals(existingUser.getUsername())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, user.getUsername());
            if (userMapper.selectOne(queryWrapper) != null) {
                return Result.error("用户名已存在");
            }
        }

        // 设置ID，确保更新的是正确的记录
        user.setId(id);

        // 如果密码被修改，需要重新加密
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null); // 不更新密码
        }

        // 更新用户信息
        userMapper.updateById(user);

        // 重新查询用户信息
        User updatedUser = userMapper.selectById(id);
        updatedUser.setPassword(null);
        return Result.success(updatedUser);
    }

    @Override
    public Result<Void> deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        userMapper.deleteById(id);
        return Result.success(null);
    }

    // 其他方法实现...
} 