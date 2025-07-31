package com.saas.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一响应结果类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "统一响应结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    @Schema(description = "响应码")
    private Integer code;
    
    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String message;
    
    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;
    
    /**
     * 时间戳
     */
    @Schema(description = "时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    public Result() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message) {
        return new Result<>(200, message);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败");
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
    
    // Getter and Setter methods
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
