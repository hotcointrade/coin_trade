package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.modular.fin.entity.TeManagementLog;
import cn.stylefeng.guns.modular.fin.mapper.TeManagementLogMapper;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.List;
/**
 * 理财认购记录Service
 */
@Service
public class TeManagementLogService extends ServiceImpl<TeManagementLogMapper, TeManagementLog>{

   
	public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }
   
	
}