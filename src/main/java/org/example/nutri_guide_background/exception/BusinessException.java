package org.example.nutri_guide_background.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    private String message;
    private Integer code;

    public BusinessException(String message) {
        this.message = message;
        this.code = 500;
    }

    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}