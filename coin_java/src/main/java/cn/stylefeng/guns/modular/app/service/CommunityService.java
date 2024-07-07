package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Community;
import cn.stylefeng.guns.modular.app.mapper.CommunityMapper;
/**
 * 官方社区Service
 *
 * @author yaying.liu
 * @since 2022-10-18 18:24:14
 */
@Service
public  class CommunityService extends ServiceImpl<CommunityMapper,Community>{

    /**
    * 查询官方社区
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除官方社区
    */
    public void deleteCommunity(Long communityId) {
        Community entity=this.baseMapper.selectById(communityId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加官方社区
    */
    public void addCommunity(Community community) {
        this.baseMapper.insert(community);
    }
}