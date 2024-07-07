package cn.stylefeng.guns.modular.otc.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Buy;
import cn.stylefeng.guns.modular.otc.mapper.BuyMapper;
/**
 * 购买挂单订单Service
 *
 * @author yaying.liu
 * @since 2020-07-13 14:52:41
 */
@Service
public  class BuyService extends ServiceImpl<BuyMapper,Buy>{

    /**
    * 查询购买挂单订单
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime,
                                                      String nickName, String orderNo, String coin, String payMethod,
                                                      String status,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime,nickName,orderNo,
                coin,payMethod,status,memberId,recommendIds);
    }

    /**
    * 删除购买挂单订单
    */
    public void deleteBuy(Long buyId) {
        Buy entity=this.baseMapper.selectById(buyId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加购买挂单订单
    */
    public void addBuy(Buy buy) {
        this.baseMapper.insert(buy);
    }

    public void cancel(Long buyId)
    {
        Buy entity=this.baseMapper.selectById(buyId);
        if(!"Y".equals(entity.getStatus()))
            return;
        entity.setStatus("N").setUpdateUser(entity.getMemberId());
        if (!updateById(entity))
            throw new UpdateDataException(100);
    }

}