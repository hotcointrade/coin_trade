package cn.stylefeng.guns.modular.bulletin.mapper;


import cn.stylefeng.guns.modular.bulletin.entity.Merchant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 商户Mapper
 *
 * @author yaying.liu
 * @Date 2019-09-02 20:11:57
 */
public interface MerchantMapper extends BaseMapper<Merchant> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("merchantId") Long merchantId);

    Page<Map<String,Object>> selectRecordByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("merchantId") Long merchantId);

}