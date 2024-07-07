package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.TeamEarnings;
import cn.stylefeng.guns.modular.app.mapper.TeamEarningsMapper;
/**
 * 团队收益Service
 *
 * @author yaying.liu
 * @since 2022-02-18 20:22:22
 */
@Service
public  class TeamEarningsService extends ServiceImpl<TeamEarningsMapper,TeamEarnings>{

    /**
    * 查询团队收益
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        Long memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
        List<Member> memberTeamForPRI = F.me().getMemberTeamForPRI(memberId);
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime,memberTeamForPRI);
    }

    /**
    * 删除团队收益
    */
    public void deleteTeamEarnings(Long teamEarningsId) {
        TeamEarnings entity=this.baseMapper.selectById(teamEarningsId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加团队收益
    */
    public void addTeamEarnings(TeamEarnings teamEarnings) {
        this.baseMapper.insert(teamEarnings);
    }
}
