package cn.stylefeng.guns.modular.mining.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.guns.modular.mining.mapper.MiningMapper;
/**
 * 矿机Service
 *
 * @author yaying.liu
 * @since 2022-06-07 13:24:41
 */
@Service
public  class MiningService extends ServiceImpl<MiningMapper,Mining>{

    /**
    * 查询矿机
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除矿机
    */
    public void deleteMining(Long miningId) {
        Mining entity=this.baseMapper.selectById(miningId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加矿机
    */
    public void addMining(Mining mining) {
        this.baseMapper.insert(mining);
    }
}