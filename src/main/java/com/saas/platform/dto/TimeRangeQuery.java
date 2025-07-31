package com.saas.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 时间范围查询DTO
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "时间范围查询参数")
public class TimeRangeQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2024-12-31 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimeRangeQuery{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                "} " + super.toString();
    }
}