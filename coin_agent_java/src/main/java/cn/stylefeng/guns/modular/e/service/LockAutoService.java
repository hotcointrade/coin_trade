package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockAuto;
import cn.stylefeng.guns.modular.e.mapper.LockAutoMapper;
/**
 * 锁仓动态收益配置Service
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:59
 */
@Service
public  class LockAutoService extends ServiceImpl<LockAutoMapper,LockAuto>{

    /**
    * 查询锁仓动态收益配置
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除锁仓动态收益配置
    */
    public void deleteLockAuto(Long lockAutoId) {
        LockAuto entity=this.baseMapper.selectById(lockAutoId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加锁仓动态收益配置
    */
    public void addLockAuto(LockAuto lockAuto) {
        this.baseMapper.insert(lockAuto);
    }
}