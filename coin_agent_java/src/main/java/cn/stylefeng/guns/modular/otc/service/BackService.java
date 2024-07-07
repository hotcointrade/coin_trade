package cn.stylefeng.guns.modular.otc.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Back;
import cn.stylefeng.guns.modular.otc.mapper.BackMapper;
/**
 * 退还押金Service
 *
 * @author yaying.liu
 * @since 2020-07-09 14:33:04
 */
@Service
public  class BackService extends ServiceImpl<BackMapper,Back>{

    /**
    * 查询退还押金
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime,memberId,recommendIds);
    }

    /**
    * 删除退还押金
    */
    public void deleteBack(Long backId) {
        Back entity=this.baseMapper.selectById(backId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加退还押金
    */
    public void addBack(Back back) {
        this.baseMapper.insert(back);
    }
}