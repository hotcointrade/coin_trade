package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PlatformCashflow;
import cn.stylefeng.guns.modular.fin.mapper.PlatformCashflowMapper;
/**
 * 平台流水Service
 *
 * @author yaying.liu
 * @Date 2020-01-03 18:06:11
 */
@Service
public  class PlatformCashflowService extends ServiceImpl<PlatformCashflowMapper,PlatformCashflow>{

    /**
    * 查询平台流水
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除平台流水
    */
    public void deletePlatformCashflow(Long platformCashflowId) {
        PlatformCashflow entity=this.baseMapper.selectById(platformCashflowId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加平台流水
    */
    public void addPlatformCashflow(PlatformCashflow platformCashflow) {
        this.baseMapper.insert(platformCashflow);
    }
}