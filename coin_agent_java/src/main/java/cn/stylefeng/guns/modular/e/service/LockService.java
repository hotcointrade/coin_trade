package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Lock;
import cn.stylefeng.guns.modular.e.mapper.LockMapper;
/**
 * 锁仓合约配置Service
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:07
 */
@Service
public  class LockService extends ServiceImpl<LockMapper,Lock>{

    /**
    * 查询锁仓合约配置
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除锁仓合约配置
    */
    public void deleteLock(Long lockId) {
        Lock entity=this.baseMapper.selectById(lockId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加锁仓合约配置
    */
    public void addLock(Lock lock) {
        this.baseMapper.insert(lock);
    }
}