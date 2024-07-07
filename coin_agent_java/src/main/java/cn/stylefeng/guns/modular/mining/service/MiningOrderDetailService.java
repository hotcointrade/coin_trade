package cn.stylefeng.guns.modular.mining.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.mining.entity.MiningOrderDetail;
import cn.stylefeng.guns.modular.mining.mapper.MiningOrderDetailMapper;
/**
 * 矿机收益Service
 *
 * @author yaying.liu
 * @since 2022-06-07 13:36:19
 */
@Service
public  class MiningOrderDetailService extends ServiceImpl<MiningOrderDetailMapper,MiningOrderDetail>{

    /**
    * 查询矿机收益
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime,memberId, recommendIds);
    }

    /**
    * 删除矿机收益
    */
    public void deleteMiningOrderDetail(Long miningOrderDetailId) {
        MiningOrderDetail entity=this.baseMapper.selectById(miningOrderDetailId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加矿机收益
    */
    public void addMiningOrderDetail(MiningOrderDetail miningOrderDetail) {
        this.baseMapper.insert(miningOrderDetail);
    }
}
