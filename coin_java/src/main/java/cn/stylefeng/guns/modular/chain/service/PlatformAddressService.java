package cn.stylefeng.guns.modular.chain.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.PlatformAddress;
import cn.stylefeng.guns.modular.chain.mapper.PlatformAddressMapper;
/**
 * 平台地址Service
 *
 * @author yaying.liu
 * @Date 2020-03-16 09:31:12
 */
@Service
public  class PlatformAddressService extends ServiceImpl<PlatformAddressMapper,PlatformAddress>{

    /**
    * 查询平台地址
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除平台地址
    */
    public void deletePlatformAddress(Long platformAddressId) {
        PlatformAddress entity=this.baseMapper.selectById(platformAddressId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加平台地址
    */
    public void addPlatformAddress(PlatformAddress platformAddress) {
        this.baseMapper.insert(platformAddress);
    }
}