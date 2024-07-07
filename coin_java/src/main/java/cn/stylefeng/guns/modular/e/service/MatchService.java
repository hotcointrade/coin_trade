package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.mapper.MatchMapper;
/**
 * 委托单信息Service
 *
 * @author yaying.liu
 * @Date 2020-03-18 10:38:28
 */
@Service
public  class MatchService extends ServiceImpl<MatchMapper,Match>{

    /**
    * 查询委托单信息
    */
    public Page<Map<String,Object>> selectByCondition(Map map) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,map);
    }

    /**
    * 删除委托单信息
    */
    public void deleteMatch(Long matchId) {
        Match entity=this.baseMapper.selectById(matchId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加委托单信息
    */
    public void addMatch(Match match) {
        this.baseMapper.insert(match);
    }
}