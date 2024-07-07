package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PayRecord;
import cn.stylefeng.guns.modular.fin.mapper.PayRecordMapper;
/**
 * 第三方接口记录表Service
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:15:58
 */
@Service
public  class PayRecordService extends ServiceImpl<PayRecordMapper,PayRecord>{

    /**
    * 查询第三方接口记录表
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除第三方接口记录表
    */
    public void deletePayRecord(Long payRecordId) {
        PayRecord entity=this.baseMapper.selectById(payRecordId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加第三方接口记录表
    */
    public void addPayRecord(PayRecord payRecord) {
        this.baseMapper.insert(payRecord);
    }
}