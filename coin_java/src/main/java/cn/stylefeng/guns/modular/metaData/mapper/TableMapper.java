package cn.stylefeng.guns.modular.metaData.mapper;

import cn.stylefeng.guns.modular.metaData.model.TableDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: yaying.liu
 * @date: 2019-09-22 17:33
 **/
public interface TableMapper extends BaseMapper<TableDto> {
    List<String> showDateBase();

    List<TableDto> getTableList(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);
}
