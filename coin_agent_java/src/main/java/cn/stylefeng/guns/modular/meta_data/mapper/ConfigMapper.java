package cn.stylefeng.guns.modular.meta_data.mapper;

import cn.stylefeng.guns.modular.meta_data.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConfigMapper extends BaseMapper<Config> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);
    Page<Map<String,Object>> selectByConditionCommon(@Param("page") Page page, @Param("condition") String condition);
    Page<Map<String,Object>> selectByConditionTeam(@Param("page") Page page, @Param("condition") String condition);
    Page<Map<String,Object>> selectByConditionSms(@Param("page") Page page, @Param("condition") String condition);
    Page<Map<String,Object>> selectByConditionEmail(@Param("page") Page page, @Param("condition") String condition);
    Page<Map<String,Object>> selectByConditionDomain(@Param("page") Page page, @Param("condition") String condition);

    void updateFlatPwd(@Param("platPwd")String platPwd);

    List<Config> selectByConditionTeamNoPage();

    Page<Map<String, Object>> selectByConditionContractOption(@Param("page") Page page, @Param("condition") String condition);

    Page<Map<String, Object>> selectByConditionCheckIn(@Param("page") Page page, @Param("condition") String condition);
}
