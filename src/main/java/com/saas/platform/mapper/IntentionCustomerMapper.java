package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.IntentionCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 意向客户 Mapper 接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface IntentionCustomerMapper extends BaseMapper<IntentionCustomer> {

    /**
     * 分页查询意向客户列表
     * 
     * @param page 分页参数
     * @param customerName 客户名称（模糊查询）
     * @param customerSource 客户来源
     * @param intentionLevel 意向级别
     * @param managerId 客户经理ID
     * @param isDeal 是否成交
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 意向客户分页列表
     */
    IPage<IntentionCustomer> selectPageWithConditions(
            Page<IntentionCustomer> page,
            @Param("customerName") String customerName,
            @Param("customerSource") String customerSource,
            @Param("intentionLevel") String intentionLevel,
            @Param("managerId") Long managerId,
            @Param("isDeal") Boolean isDeal,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 根据手机号查询意向客户
     * 
     * @param phone 手机号
     * @return 意向客户信息
     */
    IntentionCustomer selectByPhone(@Param("phone") String phone);

    /**
     * 根据客户经理ID查询意向客户列表
     * 
     * @param managerId 客户经理ID
     * @return 意向客户列表
     */
    List<IntentionCustomer> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 统计各意向级别的客户数量
     * 
     * @param managerId 客户经理ID（可选，为null时统计全部）
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByIntentionLevel(@Param("managerId") Long managerId);

    /**
     * 查询需要跟进的客户列表
     * 
     * @param managerId 客户经理ID（可选）
     * @param currentTime 当前时间
     * @return 需要跟进的客户列表
     */
    List<IntentionCustomer> selectFollowUpList(
            @Param("managerId") Long managerId,
            @Param("currentTime") LocalDateTime currentTime
    );
}