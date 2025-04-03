package org.example.nutri_guide_background.common;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private Boolean success;
    private String message;
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setSuccess(true);
        result.setMessage("操作成功");
        return result;
    }
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
} 