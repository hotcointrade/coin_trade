package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Legal;
/**
 * 法币账户Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:11:25
 */
public interface LegalMapper extends BaseMapper<Legal> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,@Param("memberId")Long memberId,@Param("recommendIds")String recommendIds);

}