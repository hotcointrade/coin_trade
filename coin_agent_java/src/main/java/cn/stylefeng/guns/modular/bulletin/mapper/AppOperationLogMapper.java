package cn.stylefeng.guns.modular.bulletin.mapper;

import cn.stylefeng.guns.modular.base.util.Query;
import cn.stylefeng.guns.modular.bulletin.entity.AppOperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppOperationLogMapper extends BaseMapper<AppOperationLog> {

    List<AppOperationLog> getLog(Query query);

    int getLogCount(Query query);

    void deleteLog();

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);
}
