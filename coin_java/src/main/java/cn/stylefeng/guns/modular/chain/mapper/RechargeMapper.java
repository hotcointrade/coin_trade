package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
/**
 * 充币记录Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-01-14 16:00:54
 */
public interface RechargeMapper extends BaseMapper<Recharge> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("param") Map<String,Object> param);

    Map total(@Param("param") Map<String,Object> param);

    List<Recharge> selectByMemberIds(@Param("memberIds")Long... memberIds);
}
