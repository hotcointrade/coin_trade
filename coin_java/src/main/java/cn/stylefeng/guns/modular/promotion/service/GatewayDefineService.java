package cn.stylefeng.guns.modular.promotion.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.promotion.entity.GatewayDefine;
import cn.stylefeng.guns.modular.promotion.mapper.GatewayDefineMapper;
/**
 * 网关定义Service
 *
 * @author yaying.liu
 * @Date 2019-10-11 11:20:42
 */
@Service
public  class GatewayDefineService extends ServiceImpl<GatewayDefineMapper,GatewayDefine>{

    /**
    * 查询网关定义
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除网关定义
    */
    public void deleteGatewayDefine(Long gatewayDefineId) {
        GatewayDefine entity=this.baseMapper.selectById(gatewayDefineId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加网关定义
    */
    public void addGatewayDefine(GatewayDefine gatewayDefine) {
        this.baseMapper.insert(gatewayDefine);
    }
}