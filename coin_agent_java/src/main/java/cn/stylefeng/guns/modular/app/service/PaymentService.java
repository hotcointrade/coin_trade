package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Payment;
import cn.stylefeng.guns.modular.app.mapper.PaymentMapper;
/**
 * 收款方式Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 11:23:03
 */
@Service
public  class PaymentService extends ServiceImpl<PaymentMapper,Payment>{

    /**
    * 查询收款方式
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除收款方式
    */
    public boolean deletePayment(Long paymentId) {
        Payment entity=this.baseMapper.selectById(paymentId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
        return true;
    }

    /**
    * 添加收款方式
    */
    public void addPayment(Payment payment) {
        this.baseMapper.insert(payment);
    }
}