package cn.stylefeng.guns.modular.e.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.e.entity.MatchDetail;
import cn.stylefeng.guns.modular.e.mapper.MatchDetailMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 撮合交易成交明细Service
 *
 * @author yaying.liu
 * @Date 2020-05-20 10:29:28
 */
@Service
public  class MatchDetailService extends ServiceImpl<MatchDetailMapper,MatchDetail>{

    /**
    * 查询撮合交易成交明细
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除撮合交易成交明细
    */
    public void deleteMatchDetail(Long matchDetailId) {
        MatchDetail entity=this.baseMapper.selectById(matchDetailId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加撮合交易成交明细
    */
    public void addMatchDetail(MatchDetail matchDetail) {
        this.baseMapper.insert(matchDetail);
    }
}