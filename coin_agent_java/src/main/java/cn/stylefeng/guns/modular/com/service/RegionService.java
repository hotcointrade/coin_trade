package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Region;
import cn.stylefeng.guns.modular.com.mapper.RegionMapper;
/**
 * 地区Service
 *
 * @author yaying.liu
 * @Date 2019-12-06 11:32:41
 */
@Service
public  class RegionService extends ServiceImpl<RegionMapper,Region>{

    /**
    * 查询地区
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除地区
    */
    public void deleteRegion(Long regionId) {
        Region entity=this.baseMapper.selectById(regionId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加地区
    */
    public void addRegion(Region region) {
        this.baseMapper.insert(region);
    }
}