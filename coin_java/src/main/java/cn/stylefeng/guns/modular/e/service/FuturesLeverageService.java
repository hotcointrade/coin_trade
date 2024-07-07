package cn.stylefeng.guns.modular.e.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.e.entity.FuturesLeverage;
import cn.stylefeng.guns.modular.e.entity.Leverage;
import cn.stylefeng.guns.modular.e.mapper.FuturesLeverageMapper;
import cn.stylefeng.guns.modular.e.mapper.LeverageMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 杠杆倍率Service
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
@Service
public  class FuturesLeverageService extends ServiceImpl<FuturesLeverageMapper, FuturesLeverage>{

    /**
    * 查询杠杆倍率
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除杠杆倍率
    */
    public void deleteLeverage(Long leverageId) {
        FuturesLeverage entity=this.baseMapper.selectById(leverageId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加杠杆倍率
    */
    public void addLeverage(FuturesLeverage leverage) {
        this.baseMapper.insert(leverage);
    }
}