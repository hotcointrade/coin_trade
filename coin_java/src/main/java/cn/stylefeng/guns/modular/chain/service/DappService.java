package cn.stylefeng.guns.modular.chain.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Dapp;
import cn.stylefeng.guns.modular.chain.mapper.DappMapper;
/**
 * 钱包调用api记录Service
 *
 * @author yaying.liu
 * @since 2020-08-18 19:46:56
 */
@Service
public  class DappService extends ServiceImpl<DappMapper,Dapp>{

    /**
    * 查询钱包调用api记录
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除钱包调用api记录
    */
    public void deleteDapp(Long dappId) {
        Dapp entity=this.baseMapper.selectById(dappId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加钱包调用api记录
    */
    public void addDapp(Dapp dapp) {
        this.baseMapper.insert(dapp);
    }
}