package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Coin;
/**
 * 用户钱包地址Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-10 18:24:36
 */
public interface CoinMapper extends BaseMapper<Coin> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}