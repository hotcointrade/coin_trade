package cn.stylefeng.guns.modular.otc.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Deposit;
/**
 * 用户押金Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-07-01 11:55:46
 */
public interface DepositMapper extends BaseMapper<Deposit> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,
                                               @Param("coin") String coin,
                                               @Param("status") String status,
                                               @Param("remark") String remark,
                                               @Param("beginTime") String beginTime, @Param("endTime") String endTime
            , @Param("memberId") Long memberId, @Param("recommendIds") String recommendIds);


    List<Map<String,Object>> total(@Param("memberId") Long memberId, @Param("recommendIds") String recommendIds);

}