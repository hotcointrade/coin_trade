package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Bought;
import cn.stylefeng.guns.modular.e.mapper.BoughtMapper;
/**
 * 法币交易Service
 *
 * @author yaying.liu
 * @Date 2020-04-03 09:48:13
 */
@Service
public  class BoughtService extends ServiceImpl<BoughtMapper,Bought>{

    /**
    * 查询法币交易
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除法币交易
    */
    public void deleteBought(Long boughtId) {
        Bought entity=this.baseMapper.selectById(boughtId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加法币交易
    */
    public void addBought(Bought bought) {
        this.baseMapper.insert(bought);
    }
}