package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PayOrder;
/**
 * 支付订单Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:14:16
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}