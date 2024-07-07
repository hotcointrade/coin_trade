package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PayRecord;
/**
 * 第三方接口记录表Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:15:58
 */
public interface PayRecordMapper extends BaseMapper<PayRecord> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}