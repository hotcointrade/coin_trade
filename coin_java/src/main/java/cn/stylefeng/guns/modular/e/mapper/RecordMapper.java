package cn.stylefeng.guns.modular.e.mapper;

import cn.stylefeng.guns.modular.app.dto.e.MatchDto;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 委托单信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-18 10:38:28
 */
public interface RecordMapper extends BaseMapper<Record> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("param") Map map);

    Match getMatch(@Param("orderNo")String orderNo);

    int updateMatch(Match sell);

    IPage<MatchDto> getPages(Page page, @Param("memberId") Long memberId, @Param("type")int type);


    IPage<Match> getPagesList(Page page, @Param("memberId") Long memberId, @Param("type") int type,@Param("symbols") String symbols);

    Record getLastUnit();

    //今日交易数据
    List<Record> getMatchList();

    /**
     * 获取时间范围内成交数据
     * @param token 币种
     * @param now from
     * @param nextMin to
     * @return
     */
    List<Record> getMatchRangeTime(@Param("token")String token, @Param("now")long now, @Param("nextMin")long nextMin);

    /**
     *
     * 获取最优订单
     *
     * 情况1：
     * 获取最优卖单列表  -入参买单： 卖-单价 <= 买-单价 ；卖单单价排序 最低价优先
     *
     * 情况2：
     * 获取最优买单列表  -入参卖单： 卖-单价 <= 买-单价 ；买单单价排序 最高价优先
     * @param match 入参买（卖）单
     * @return
     */
    List<Record> getBestOrderList(Record match);





}