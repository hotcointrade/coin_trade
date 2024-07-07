package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.modular.fin.entity.TeManagement;
import cn.stylefeng.guns.modular.fin.mapper.TeManagementMapper;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.List;
/**
 * 理财认购币Service
 */
@Service
public class TeManagementService extends ServiceImpl<TeManagementMapper, TeManagement>{

   
	public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }
   
	
}