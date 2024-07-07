package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.TeamEarning;
import cn.stylefeng.guns.modular.fin.mapper.TeamEarningMapper;
/**
 * 团队佣金Service
 *
 * @author yaying.liu
 * @since 2022-02-18 18:53:54
 */
@Service
public  class TeamEarningService extends ServiceImpl<TeamEarningMapper,TeamEarning>{

    /**
    * 查询团队佣金
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除团队佣金
    */
    public void deleteTeamEarning(Long teamEarningId) {
        TeamEarning entity=this.baseMapper.selectById(teamEarningId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加团队佣金
    */
    public void addTeamEarning(TeamEarning teamEarning) {
        this.baseMapper.insert(teamEarning);
    }
}