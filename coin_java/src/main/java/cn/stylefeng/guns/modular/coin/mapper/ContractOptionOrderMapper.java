package cn.stylefeng.guns.modular.coin.mapper;

import cn.stylefeng.guns.modular.coin.entity.ContractOption;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 期权交易对Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
public interface ContractOptionOrderMapper extends BaseMapper<ContractOptionOrder> {

   // Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    Page<Map<String,Object>> selectByCondition(@Param("page") Page page,
                                               @Param("symbol") String symbol,
                                               @Param("memberId") Long memberId,
                                               @Param("betAmount") BigDecimal betAmount,
                                               @Param("rewardAmount") BigDecimal rewardAmount);

}