package cn.stylefeng.guns.modular.mining.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.mapper.MiningOrderMapper;
/**
 * 矿机订单Service
 *
 * @author yaying.liu
 * @since 2022-06-08 21:02:12
 */
@Service
public  class MiningOrderService extends ServiceImpl<MiningOrderMapper,MiningOrder>{

    /**
    * 查询矿机订单
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除矿机订单
    */
    public void deleteMiningOrder(Long miningOrderId) {
        MiningOrder entity=this.baseMapper.selectById(miningOrderId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加矿机订单
    */
    public void addMiningOrder(MiningOrder miningOrder) {
        this.baseMapper.insert(miningOrder);
    }
}