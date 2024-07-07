package cn.stylefeng.guns.modular.coin.mapper;

import cn.stylefeng.guns.modular.coin.entity.Futures;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 合约交易对Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
public interface FuturesMapper extends BaseMapper<Futures> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}