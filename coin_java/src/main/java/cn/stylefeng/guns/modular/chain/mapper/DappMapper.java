package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Dapp;
/**
 * 钱包调用api记录Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-08-18 19:46:56
 */
public interface DappMapper extends BaseMapper<Dapp> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}