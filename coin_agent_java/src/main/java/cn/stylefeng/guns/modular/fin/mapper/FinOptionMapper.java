package cn.stylefeng.guns.modular.fin.mapper;

import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.entity.FinOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 币币账户Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
public interface FinOptionMapper extends BaseMapper<FinOption> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,@Param("memberId")Long memberId,@Param("recommendIds")String recommendIds);

    int updateWallet(FinOption entity);

    List<FinOption> zeroList();
}