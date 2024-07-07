package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockRecord;
import cn.stylefeng.guns.modular.e.mapper.LockRecordMapper;
/**
 * 锁仓记录Service
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:59
 */
@Service
public  class LockRecordService extends ServiceImpl<LockRecordMapper,LockRecord>{

    /**
    * 查询锁仓记录
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除锁仓记录
    */
    public void deleteLockRecord(Long lockRecordId) {
        LockRecord entity=this.baseMapper.selectById(lockRecordId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加锁仓记录
    */
    public void addLockRecord(LockRecord lockRecord) {
        this.baseMapper.insert(lockRecord);
    }
}