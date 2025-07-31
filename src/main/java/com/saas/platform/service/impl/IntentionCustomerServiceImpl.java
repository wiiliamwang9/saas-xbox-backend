package com.saas.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.dto.IntentionCustomerDTO;
import com.saas.platform.dto.IntentionCustomerQuery;
import com.saas.platform.entity.IntentionCustomer;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.IntentionCustomerMapper;
import com.saas.platform.service.IntentionCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 意向客户服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IntentionCustomerServiceImpl extends ServiceImpl<IntentionCustomerMapper, IntentionCustomer> 
        implements IntentionCustomerService {

    @Override
    public IPage<IntentionCustomer> pageQuery(IntentionCustomerQuery query) {
        Page<IntentionCustomer> page = new Page<>(query.getCurrent(), query.getSize());
        
        return baseMapper.selectPageWithConditions(
                page,
                query.getCustomerName(),
                query.getCustomerSource(),
                query.getIntentionLevel(),
                query.getManagerId(),
                query.getIsDeal(),
                query.getStartTime(),
                query.getEndTime()
        );
    }

    @Override
    public boolean saveIntentionCustomer(IntentionCustomerDTO dto) {
        // 验证手机号是否已存在
        if (StrUtil.isNotBlank(dto.getPhone()) && isPhoneExists(dto.getPhone(), null)) {
            throw new BusinessException("该手机号已存在");
        }
        
        IntentionCustomer entity = new IntentionCustomer();
        BeanUtil.copyProperties(dto, entity);
        
        // 设置默认值
        if (entity.getLastContactTime() == null) {
            entity.setLastContactTime(LocalDateTime.now());
        }
        
        return this.save(entity);
    }

    @Override
    public boolean updateIntentionCustomer(IntentionCustomerDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("客户ID不能为空");
        }
        
        // 验证客户是否存在
        IntentionCustomer existCustomer = this.getById(dto.getId());
        if (existCustomer == null) {
            throw new BusinessException("客户不存在");
        }
        
        // 验证手机号是否已存在（排除当前客户）
        if (StrUtil.isNotBlank(dto.getPhone()) && isPhoneExists(dto.getPhone(), dto.getId())) {
            throw new BusinessException("该手机号已存在");
        }
        
        IntentionCustomer entity = new IntentionCustomer();
        BeanUtil.copyProperties(dto, entity);
        
        return this.updateById(entity);
    }

    @Override
    public boolean deleteIntentionCustomer(Long id) {
        if (id == null) {
            throw new BusinessException("客户ID不能为空");
        }
        
        IntentionCustomer customer = this.getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        // 软删除
        return this.removeById(id);
    }

    @Override
    public boolean batchDeleteIntentionCustomer(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BusinessException("客户ID列表不能为空");
        }
        
        // 批量软删除
        return this.removeByIds(ids);
    }

    @Override
    public IntentionCustomer getByPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return null;
        }
        
        return baseMapper.selectByPhone(phone);
    }

    @Override
    public List<IntentionCustomer> getByManagerId(Long managerId) {
        if (managerId == null) {
            return CollUtil.newArrayList();
        }
        
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<Map<String, Object>> countByIntentionLevel(Long managerId) {
        return baseMapper.countByIntentionLevel(managerId);
    }

    @Override
    public List<IntentionCustomer> getFollowUpList(Long managerId) {
        return baseMapper.selectFollowUpList(managerId, LocalDateTime.now());
    }

    @Override
    public List<IntentionCustomer> exportData(IntentionCustomerQuery query) {
        // 构建查询条件
        LambdaQueryWrapper<IntentionCustomer> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(StrUtil.isNotBlank(query.getCustomerName()), 
                IntentionCustomer::getCustomerName, query.getCustomerName())
                .eq(StrUtil.isNotBlank(query.getCustomerSource()), 
                        IntentionCustomer::getCustomerSource, query.getCustomerSource())
                .eq(StrUtil.isNotBlank(query.getIntentionLevel()), 
                        IntentionCustomer::getIntentionLevel, query.getIntentionLevel())
                .eq(query.getManagerId() != null, 
                        IntentionCustomer::getManagerId, query.getManagerId())
                .eq(query.getIsDeal() != null, 
                        IntentionCustomer::getIsDeal, query.getIsDeal())
                .ge(query.getStartTime() != null, 
                        IntentionCustomer::getCreatedAt, query.getStartTime())
                .le(query.getEndTime() != null, 
                        IntentionCustomer::getCreatedAt, query.getEndTime())
                .orderByDesc(IntentionCustomer::getCreatedAt);
        
        return this.list(wrapper);
    }

    @Override
    public boolean isPhoneExists(String phone, Long excludeId) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        
        LambdaQueryWrapper<IntentionCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntentionCustomer::getPhone, phone);
        
        if (excludeId != null) {
            wrapper.ne(IntentionCustomer::getId, excludeId);
        }
        
        return this.count(wrapper) > 0;
    }
}