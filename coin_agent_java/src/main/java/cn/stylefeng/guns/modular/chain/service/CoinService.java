package cn.stylefeng.guns.modular.chain.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Coin;
import cn.stylefeng.guns.modular.chain.mapper.CoinMapper;
/**
 * 用户钱包地址Service
 *
 * @author yaying.liu
 * @Date 2019-12-10 18:24:36
 */
@Service
public  class CoinService extends ServiceImpl<CoinMapper,Coin>{

    /**
    * 查询用户钱包地址
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除用户钱包地址
    */
    public void deleteCoin(Long coinId) {
        Coin entity=this.baseMapper.selectById(coinId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加用户钱包地址
    */
    public void addCoin(Coin coin) {
        this.baseMapper.insert(coin);
    }
}