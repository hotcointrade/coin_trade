package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.modular.fin.entity.TeManagementRelease;
import cn.stylefeng.guns.modular.fin.mapper.TeManagementReleaseMapper;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.List;
/**
 * 理财释放记录Service
 */
@Service
public class TeManagementReleaseService extends ServiceImpl<TeManagementReleaseMapper, TeManagementRelease>{


	public Page<Map<String, Object>> selectByCondition(String condition,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,memberId,recommendIds);
    }


}
