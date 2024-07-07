package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Live;
import cn.stylefeng.guns.modular.e.mapper.LiveMapper;
/**
 * 生活支付开通记录Service
 *
 * @author yaying.liu
 * @Date 2020-06-28 16:17:00
 */
@Service
public  class LiveService extends ServiceImpl<LiveMapper,Live>{

    /**
    * 查询生活支付开通记录
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除生活支付开通记录
    */
    public void deleteLive(Long liveId) {
        Live entity=this.baseMapper.selectById(liveId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加生活支付开通记录
    */
    public void addLive(Live live) {
        this.baseMapper.insert(live);
    }
}