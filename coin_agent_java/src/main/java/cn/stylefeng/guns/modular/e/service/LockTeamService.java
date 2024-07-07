package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockTeam;
import cn.stylefeng.guns.modular.e.mapper.LockTeamMapper;
/**
 * 锁仓团队奖配置Service
 *
 * @author yaying.liu
 * @since 2020-09-25 10:45:37
 */
@Service
public  class LockTeamService extends ServiceImpl<LockTeamMapper,LockTeam>{

    /**
    * 查询锁仓团队奖配置
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除锁仓团队奖配置
    */
    public void deleteLockTeam(Long lockTeamId) {
        LockTeam entity=this.baseMapper.selectById(lockTeamId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加锁仓团队奖配置
    */
    public void addLockTeam(LockTeam lockTeam) {
        this.baseMapper.insert(lockTeam);
    }
}