package cn.stylefeng.guns.modular.fin.mapper;

import cn.stylefeng.guns.modular.fin.entity.FinOption;
import cn.stylefeng.guns.modular.fin.entity.Periodic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 理财生息 接口
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
public interface FinPeriodicMapper extends BaseMapper<Periodic> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);


}