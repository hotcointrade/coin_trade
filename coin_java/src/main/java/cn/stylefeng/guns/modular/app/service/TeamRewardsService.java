package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.TeamRewards;
import cn.stylefeng.guns.modular.app.mapper.TeamRewardsMapper;
/**
 * 社区奖励Service
 *
 * @author yaying.liu
 * @since 2022-07-07 17:45:57
 */
@Service
public  class TeamRewardsService extends ServiceImpl<TeamRewardsMapper,TeamRewards>{

    /**
    * 查询社区奖励
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除社区奖励
    */
    public void deleteTeamRewards(Long teamRewardsId) {
        TeamRewards entity=this.baseMapper.selectById(teamRewardsId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加社区奖励
    */
    public void addTeamRewards(TeamRewards teamRewards) {
        this.baseMapper.insert(teamRewards);
    }
}