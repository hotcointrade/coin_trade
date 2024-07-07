package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import cn.stylefeng.guns.modular.fin.entity.Periodic;
import cn.stylefeng.guns.modular.fin.mapper.FinPeriodicMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 理财生息配置Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
@Service
public class FinPeriodicService extends ServiceImpl<FinPeriodicMapper, Periodic>
{

    /**
     * 查询
     */
    public Page<Map<String, Object>> selectByCondition(String condition)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition);
    }

    /**
     * 删除
     */
    public void deletePeriodic(Long currencyId)
    {
        Periodic entity = this.baseMapper.selectById(currencyId);
        //将删除标志设置为Y，表示删除
        entity.setIsDelete("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加
     */
    public void addPeriodic(Periodic currency)
    {
        this.baseMapper.insert(currency);
    }


}