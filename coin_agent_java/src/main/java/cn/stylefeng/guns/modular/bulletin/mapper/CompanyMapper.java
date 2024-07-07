package cn.stylefeng.guns.modular.bulletin.mapper;


import cn.stylefeng.guns.modular.bulletin.entity.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 平台账户Mapper
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:52:51
 */
public interface CompanyMapper extends BaseMapper<Company> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}