package cn.stylefeng.guns.modular.otc.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Buy;
/**
 * 购买挂单订单Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-07-13 14:52:41
 */
public interface BuyMapper extends BaseMapper<Buy> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("nickName") String nickName
            , @Param("orderNo") String orderNo
            , @Param("coin") String coin
            , @Param("payMethod") String payMethod
            , @Param("status") String status, @Param("memberId") Long memberId
            , @Param("recommendIds") String recommendIds
    );

}