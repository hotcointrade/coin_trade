package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockRecord;
/**
 * 锁仓记录Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:58
 */
public interface LockRecordMapper extends BaseMapper<LockRecord> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    BigDecimal autoStandardSum(@Param("memberId")Long memberId, @Param("lockIds")String[] lockIds,@Param("status")String status);
}