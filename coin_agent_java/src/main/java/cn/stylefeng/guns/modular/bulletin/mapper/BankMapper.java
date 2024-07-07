package cn.stylefeng.guns.modular.bulletin.mapper;


import cn.stylefeng.guns.modular.bulletin.entity.Bank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 银行列表Mapper
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:24:35
 */
public interface BankMapper extends BaseMapper<Bank> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}