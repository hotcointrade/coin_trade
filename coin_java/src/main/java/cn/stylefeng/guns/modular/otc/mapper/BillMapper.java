package cn.stylefeng.guns.modular.otc.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Bill;
/**
 * 交易结算订单Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-07-13 15:03:40
 */
public interface BillMapper extends BaseMapper<Bill> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime
            , @Param("status") String status
            , @Param("buyAccount") String buyAccount
            , @Param("buyName") String buyName
            , @Param("sellAccount") String sellAccount
            , @Param("sellName") String sellName
            , @Param("payMethod") String payMethod
            , @Param("orderNo") String orderNo
            , @Param("markNo") String markNo, @Param("memberId") Long memberId
            , @Param("recommendIds") String recommendIds);

    Page<Map<String,Object>> selectAppeal(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime
            , @Param("status") String status
            , @Param("buyAccount") String buyAccount
            , @Param("buyName") String buyName
            , @Param("sellAccount") String sellAccount
            , @Param("sellName") String sellName
            , @Param("payMethod") String payMethod
            , @Param("orderNo") String orderNo
            , @Param("markNo") String markNo
            , @Param("appealStatus") String appealStatus
            , @Param("duty") String duty
            , @Param("memberId") Long memberId
            , @Param("recommendIds") String recommendIds
    );

    int inOrder(@Param("memberId") Long memberId);
}