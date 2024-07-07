package cn.stylefeng.guns.modular.promotion.mapper;

import cn.stylefeng.guns.modular.promotion.entity.WhiteBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;


public interface WhiteBookMapper extends BaseMapper<WhiteBook> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}