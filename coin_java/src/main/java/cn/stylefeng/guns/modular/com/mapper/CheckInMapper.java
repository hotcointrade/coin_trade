package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.CheckIn;
/**
 * 签到Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-09 16:28:04
 */
public interface CheckInMapper extends BaseMapper<CheckIn> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);
    //获取用户本月签到信息
    List<CheckIn> getCheckListByMemberId(@Param("memberId") Long memberId);

    int getCountByMemberId(@Param("memberId") Long memberId);

    int isTodayCheck(@Param("memberId")Long memberId);
}