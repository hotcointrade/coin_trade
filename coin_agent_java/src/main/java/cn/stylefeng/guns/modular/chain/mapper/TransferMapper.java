package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Transfer;
/**
 * 转账信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-01-14 17:08:46
 */
public interface TransferMapper extends BaseMapper<Transfer> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}