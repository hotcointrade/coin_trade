package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Compact;
/**
 * 合约订单Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-23 14:35:24
 */
public interface CompactMapper extends BaseMapper<Compact> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("map")Map map );

    List<Long> getMemberIdList();

    List<Compact> getEntrust();

    int updateEntrust(Compact dto);

    /**
     *
     * @param memberId id
     * @param coin 币种
     * @return
     */
    BigDecimal handleTotal(@Param("memberId")Long memberId, @Param("coin")String coin);
    /**
     * 持仓保证金计算
     *
     * @param memberId
     * @param coin 币种
     * @return
     */
    BigDecimal getPositionTotal(@Param("memberId")Long memberId,@Param("coin")String coin);
    //
    BigDecimal getPositionTotalFix(@Param("memberId")Long memberId,@Param("coin")String coin);

    BigDecimal getIn(@Param("memberId")Long memberId,@Param("coin")String coin);

    BigDecimal getPositionTotalStatus(@Param("memberId")Long memberId, @Param("status")String status,@Param("coin")String coin);

    int updateBoomBeforeOrder(@Param("memberId")Long memberId,@Param("updateTime")Date currentTime,@Param("coin")String coin);

    int updateCross(@Param("compactId")Long compactId, @Param("partPrice")BigDecimal partPrice);

    int getHold(@Param("memberId")Long memberId,  @Param("coin")String coin);

    List<Long> getMidsByInAndN();

    Map<String, Object> getTotalMap(Map<String, Object> map);
}