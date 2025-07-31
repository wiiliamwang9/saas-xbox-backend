package com.saas.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

/**
 * 分页查询基础DTO
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "分页查询参数")
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "20")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能大于100")
    private Integer size = 20;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段", example = "createdAt")
    private String orderBy;

    /**
     * 排序方向
     */
    @Schema(description = "排序方向", allowableValues = {"ASC", "DESC"}, example = "DESC")
    private String orderDirection = "DESC";

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "current=" + current +
                ", size=" + size +
                ", orderBy='" + orderBy + '\'' +
                ", orderDirection='" + orderDirection + '\'' +
                '}';
    }
}