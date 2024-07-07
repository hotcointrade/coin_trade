package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.PhoneCode;
/**
 * 电话区号Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-20 17:59:48
 */
public interface PhoneCodeMapper extends BaseMapper<PhoneCode> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}