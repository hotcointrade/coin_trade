package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PayOrder;
import cn.stylefeng.guns.modular.fin.mapper.PayOrderMapper;
/**
 * 支付订单Service
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:14:16
 */
@Service
public  class PayOrderService extends ServiceImpl<PayOrderMapper,PayOrder>{

    /**
    * 查询支付订单
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除支付订单
    */
    public void deletePayOrder(Long payOrderId) {
        PayOrder entity=this.baseMapper.selectById(payOrderId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加支付订单
    */
    public void addPayOrder(PayOrder payOrder) {
        this.baseMapper.insert(payOrder);
    }
}