package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.TeamLevel;
import cn.stylefeng.guns.modular.app.mapper.TeamLevelMapper;
/**
 * 团队返佣Service
 *
 * @author yaying.liu
 * @since 2022-08-22 14:46:40
 */
@Service
public  class TeamLevelService extends ServiceImpl<TeamLevelMapper,TeamLevel>{

    /**
    * 查询团队返佣
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除团队返佣
    */
    public void deleteTeamLevel(Long teamLevelId) {
        TeamLevel entity=this.baseMapper.selectById(teamLevelId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加团队返佣
    */
    public void addTeamLevel(TeamLevel teamLevel) {
        this.baseMapper.insert(teamLevel);
    }
}