package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.dto.IntentionCustomerDTO;
import com.saas.platform.dto.IntentionCustomerQuery;
import com.saas.platform.entity.IntentionCustomer;

import java.util.List;
import java.util.Map;

/**
 * 意向客户服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface IntentionCustomerService extends IService<IntentionCustomer> {

    /**
     * 分页查询意向客户
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<IntentionCustomer> pageQuery(IntentionCustomerQuery query);

    /**
     * 保存意向客户
     * 
     * @param dto 意向客户信息
     * @return 是否成功
     */
    boolean saveIntentionCustomer(IntentionCustomerDTO dto);

    /**
     * 更新意向客户
     * 
     * @param dto 意向客户信息
     * @return 是否成功
     */
    boolean updateIntentionCustomer(IntentionCustomerDTO dto);

    /**
     * 根据ID删除意向客户（软删除）
     * 
     * @param id 客户ID
     * @return 是否成功
     */
    boolean deleteIntentionCustomer(Long id);

    /**
     * 根据ID批量删除意向客户（软删除）
     * 
     * @param ids 客户ID列表
     * @return 是否成功
     */
    boolean batchDeleteIntentionCustomer(List<Long> ids);

    /**
     * 根据手机号查询意向客户
     * 
     * @param phone 手机号
     * @return 意向客户信息
     */
    IntentionCustomer getByPhone(String phone);

    /**
     * 根据客户经理ID查询意向客户列表
     * 
     * @param managerId 客户经理ID
     * @return 意向客户列表
     */
    List<IntentionCustomer> getByManagerId(Long managerId);

    /**
     * 统计各意向级别的客户数量
     * 
     * @param managerId 客户经理ID（可选）
     * @return 统计结果
     */
    List<Map<String, Object>> countByIntentionLevel(Long managerId);

    /**
     * 查询需要跟进的客户列表
     * 
     * @param managerId 客户经理ID（可选）
     * @return 需要跟进的客户列表
     */
    List<IntentionCustomer> getFollowUpList(Long managerId);

    /**
     * 导出意向客户数据
     * 
     * @param query 查询条件
     * @return 导出数据
     */
    List<IntentionCustomer> exportData(IntentionCustomerQuery query);

    /**
     * 验证手机号是否已存在
     * 
     * @param phone 手机号
     * @param excludeId 排除的客户ID（用于更新时的验证）
     * @return 是否存在
     */
    boolean isPhoneExists(String phone, Long excludeId);
}