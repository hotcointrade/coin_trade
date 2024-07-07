package cn.stylefeng.guns.modular.otc.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Appeal;
import cn.stylefeng.guns.modular.otc.mapper.AppealMapper;
/**
 * 申诉订单Service
 *
 * @author yaying.liu
 * @since 2020-07-15 17:16:25
 */
@Service
public  class AppealService extends ServiceImpl<AppealMapper,Appeal>{

    /**
    * 查询申诉订单
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除申诉订单
    */
    public void deleteAppeal(Long appealId) {
        Appeal entity=this.baseMapper.selectById(appealId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加申诉订单
    */
    public void addAppeal(Appeal appeal) {
        this.baseMapper.insert(appeal);
    }
}