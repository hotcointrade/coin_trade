package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockProfit;
import cn.stylefeng.guns.modular.e.mapper.LockProfitMapper;
/**
 * 锁仓静态收益记录Service
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:24
 */
@Service
public  class LockProfitService extends ServiceImpl<LockProfitMapper,LockProfit>{

    /**
    * 查询锁仓静态收益记录
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除锁仓静态收益记录
    */
    public void deleteLockProfit(Long lockProfitId) {
        LockProfit entity=this.baseMapper.selectById(lockProfitId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加锁仓静态收益记录
    */
    public void addLockProfit(LockProfit lockProfit) {
        this.baseMapper.insert(lockProfit);
    }
}