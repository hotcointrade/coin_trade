package cn.stylefeng.guns.modular.e.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.e.entity.Compact;
import cn.stylefeng.guns.modular.e.entity.FuturesCompact;
import cn.stylefeng.guns.modular.e.mapper.CompactMapper;
import cn.stylefeng.guns.modular.e.mapper.FuturesCompactMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * qihuo订单Service
 *
 * @author yaying.liu
 * @Date 2020-03-23 14:35:24
 */
@Service
public  class FuturesCompactService extends ServiceImpl<FuturesCompactMapper, FuturesCompact>{

    /**
    * 查询合约订单
    */
    public Page<Map<String,Object>> selectByCondition(Map<String,Object> map) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,map);
    }

    /**
    * 删除合约订单
    */
    public void deleteCompact(Long compactId) {
        FuturesCompact entity=this.baseMapper.selectById(compactId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加合约订单
    */
    public void addCompact(FuturesCompact compact) {
        this.baseMapper.insert(compact);
    }

    public Map<String, Object> getTotalMap(Map<String,Object> map) {
        return this.baseMapper.getTotalMap(map);
    }
}