package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LiveRecord;
/**
 * 生活支付打款Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-06-29 17:01:20
 */
public interface LiveRecordMapper extends BaseMapper<LiveRecord> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page
            , @Param("condition") String condition
            , @Param("orderNo") String orderNo
            , @Param("status") String status
    );

}