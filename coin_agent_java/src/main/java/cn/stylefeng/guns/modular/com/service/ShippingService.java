package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Shipping;
import cn.stylefeng.guns.modular.com.mapper.ShippingMapper;
/**
 * 收货地址Service
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:11:22
 */
@Service
public  class ShippingService extends ServiceImpl<ShippingMapper,Shipping>{

    /**
    * 查询收货地址
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除收货地址
    */
    public void deleteShipping(Long shippingId) {
        Shipping entity=this.baseMapper.selectById(shippingId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加收货地址
    */
    public void addShipping(Shipping shipping) {
        this.baseMapper.insert(shipping);
    }
}