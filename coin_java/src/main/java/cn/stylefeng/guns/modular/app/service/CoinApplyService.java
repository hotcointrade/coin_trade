package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.CoinApply;
import cn.stylefeng.guns.modular.app.mapper.CoinApplyMapper;
/**
 * 上币申请Service
 *
 * @author yaying.liu
 * @since 2022-02-20 20:37:01
 */
@Service
public  class CoinApplyService extends ServiceImpl<CoinApplyMapper,CoinApply>{

    /**
    * 查询上币申请
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除上币申请
    */
    public void deleteCoinApply(Long coinApplyId) {
        CoinApply entity=this.baseMapper.selectById(coinApplyId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加上币申请
    */
    public void addCoinApply(CoinApply coinApply) {
        this.baseMapper.insert(coinApply);
    }
}