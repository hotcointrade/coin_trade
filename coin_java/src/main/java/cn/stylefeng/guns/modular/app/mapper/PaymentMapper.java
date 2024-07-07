package cn.stylefeng.guns.modular.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Payment;
/**
 * 收款方式Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-12 11:23:03
 */
public interface PaymentMapper extends BaseMapper<Payment> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}