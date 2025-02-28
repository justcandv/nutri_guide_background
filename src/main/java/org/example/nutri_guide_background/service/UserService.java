package org.example.nutri_guide_background.service;

import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.UserRegisterDTO;
import org.example.nutri_guide_background.entity.User;

import java.util.Map;

public interface UserService {
    Result<User> register(UserRegisterDTO registerDTO);
    Result<Map<String, Object>> login(String username, String password);
    Result<User> getUserById(Long id);
    Result<User> updateUser(Long id, User user);
    Result<Void> deleteUser(Long id);

} 