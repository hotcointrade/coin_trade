package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Withdraw;
/**
 * 提币信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:05:21
 */
public interface WithdrawMapper extends BaseMapper<Withdraw> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("param") Map param);

    Map<String,Object> total(@Param("param") Map param);

    BigDecimal getSummary();

    BigDecimal getSummaryToday();

    List<Withdraw> selectByMemberIds(@Param("memberIds")Long... memberIds);
}
