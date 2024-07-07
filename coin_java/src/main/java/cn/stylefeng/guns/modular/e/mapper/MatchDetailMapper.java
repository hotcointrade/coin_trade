package cn.stylefeng.guns.modular.e.mapper;

import cn.stylefeng.guns.modular.e.entity.MatchDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 撮合交易成交明细Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-05-20 10:29:28
 */
public interface MatchDetailMapper extends BaseMapper<MatchDetail>
{

    Page<Map<String, Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

    //今日交易数据
    List<MatchDetail> getMatchList(@Param("token") String token);

    /**
     * 获取时间范围内成交数据
     *
     * @param token   币种
     * @param now     from
     * @param nextMin to
     * @return
     */
    List<MatchDetail> getMatchRangeTime(@Param("token") String token, @Param("now") long now, @Param("nextMin") long nextMin);


    /**
     * 根据订单号获取成交明细列表
     *
     * @param order 订单号
     * @return
     */
    List<MatchDetail> getMatchListByOrderNo(@Param("order") String order);

}