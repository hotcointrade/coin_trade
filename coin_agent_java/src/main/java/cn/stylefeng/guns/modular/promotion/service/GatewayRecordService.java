package cn.stylefeng.guns.modular.promotion.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.promotion.entity.GatewayRecord;
import cn.stylefeng.guns.modular.promotion.mapper.GatewayRecordMapper;
/**
 * 网关记录Service
 *
 * @author yaying.liu
 * @Date 2019-10-11 10:44:44
 */
@Service
public  class GatewayRecordService extends ServiceImpl<GatewayRecordMapper,GatewayRecord>{

    /**
    * 查询网关记录
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除网关记录
    */
    public void deleteGatewayRecord(Long gatewayRecordId) {
        GatewayRecord entity=this.baseMapper.selectById(gatewayRecordId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加网关记录
    */
    public void addGatewayRecord(GatewayRecord gatewayRecord) {
        this.baseMapper.insert(gatewayRecord);
    }
}