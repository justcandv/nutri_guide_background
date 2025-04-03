package org.example.nutri_guide_background.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.nutri_guide_background.entity.Delivery;

/**
 * 配送数据访问接口
 */
@Mapper
public interface DeliveryMapper extends BaseMapper<Delivery> {
} 