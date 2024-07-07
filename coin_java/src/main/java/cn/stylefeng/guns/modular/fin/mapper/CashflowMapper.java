package cn.stylefeng.guns.modular.fin.mapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Cashflow;
/**
 * 流水记录Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:14
 */
public interface CashflowMapper extends BaseMapper<Cashflow> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition
            ,@Param("flowType")String flowType
            ,@Param("itemCode")String  itemCode
            ,@Param("memberId")Long  memberId
            ,@Param("recommendIds")String  recommendIds
    );

    BigDecimal getBtcWithdrawalPrice(@Param("memberId") Long memberId);



    BigDecimal getCurrentPerformance(@Param("flowType")String flowType,@Param("coin")String coin,@Param("memberId")Long memberId);

    List<Cashflow> getCurrentBuyPowerCashflow();

    BigDecimal getTotalPrice(@Param("teamMember")List<Member> teamMember);

    List<Cashflow> getWillExpire(@Param("powerDay") Long powerDay);

    List<Cashflow> getCurrentPerformanceCashList(@Param("memberId")Long memberId);

    BigDecimal getSumNewBuyPriceByArea(@Param("member")Member member);

    BigDecimal getBuyPowerForDirectNumber(@Param("memberId")Long memberId);

    BigDecimal getTodayNewPower();
    //今日挖矿收益
    BigDecimal getTodayBtc();
    //当日总团队业绩
    BigDecimal totalTodayPerformance(@Param("flowType")String flowType,@Param("coin")String coin,@Param("memberId")Long memberId);
    //获取用户指定流水总额
    BigDecimal getSumPrice(@Param("memberId") Long memberId, @Param("flowType") String flowType);

    //今日/累计 排单总量
    BigDecimal bossTotal(@Param("i") Long i);

    BigDecimal teamToday(@Param("memberId") Long memberId,@Param("i") Long i);

    Map<String,Object> teamAwardList(@Param("memberId") Long memberId);

    BigDecimal teamAwardSum(@Param("memberId") Long memberId);

    //计算合约资产

    /**
     * 计算合约资产
     * @param memberId 用户id
     * @param flowType 流水类型
     * @param coin 币种
     * @return
     */
    BigDecimal getBalance(@Param("memberId")Long memberId,@Param("flowType")String flowType,@Param("coin")String coin);

    int updateConvert(@Param("memberId")Long memberId,@Param("coin") String coin);
}