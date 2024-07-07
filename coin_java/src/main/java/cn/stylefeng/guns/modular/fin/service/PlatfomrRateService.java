package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PlatfomrRate;
import cn.stylefeng.guns.modular.fin.mapper.PlatfomrRateMapper;
/**
 * 系统汇率Service
 *
 * @author yaying.liu
 * @since 2022-10-18 13:18:11
 */
@Service
public  class PlatfomrRateService extends ServiceImpl<PlatfomrRateMapper,PlatfomrRate>{

    /**
    * 查询系统汇率
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        page.setAsc("sort");
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除系统汇率
    */
    public void deletePlatfomrRate(Long platfomrRateId) {
        PlatfomrRate entity=this.baseMapper.selectById(platfomrRateId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加系统汇率
    */
    public void addPlatfomrRate(PlatfomrRate platfomrRate) {
        this.baseMapper.insert(platfomrRate);
    }
}
