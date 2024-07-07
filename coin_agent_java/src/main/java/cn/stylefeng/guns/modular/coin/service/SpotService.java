package cn.stylefeng.guns.modular.coin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.mapper.SpotMapper;
/**
 * 现货交易对Service
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
@Service
public  class SpotService extends ServiceImpl<SpotMapper,Spot>{

    /**
    * 查询现货交易对
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除现货交易对
    */
    public void deleteSpot(Long spotId) {

        removeById(spotId);
    }

    /**
    * 添加现货交易对
    */
    public void addSpot(Spot spot) {
        this.baseMapper.insert(spot);
    }
}