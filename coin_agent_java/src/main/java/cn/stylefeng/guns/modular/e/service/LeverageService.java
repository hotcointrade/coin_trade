package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Leverage;
import cn.stylefeng.guns.modular.e.mapper.LeverageMapper;
/**
 * 杠杆倍率Service
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
@Service
public  class LeverageService extends ServiceImpl<LeverageMapper,Leverage>{

    /**
    * 查询杠杆倍率
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除杠杆倍率
    */
    public void deleteLeverage(Long leverageId) {
        Leverage entity=this.baseMapper.selectById(leverageId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加杠杆倍率
    */
    public void addLeverage(Leverage leverage) {
        this.baseMapper.insert(leverage);
    }
}