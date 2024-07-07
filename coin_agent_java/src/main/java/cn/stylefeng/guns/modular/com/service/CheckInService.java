package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.CheckIn;
import cn.stylefeng.guns.modular.com.mapper.CheckInMapper;
/**
 * 签到Service
 *
 * @author yaying.liu
 * @Date 2019-12-09 16:28:04
 */
@Service
public  class CheckInService extends ServiceImpl<CheckInMapper,CheckIn>{

    /**
    * 查询签到
    */
    public Page<Map<String,Object>> selectByCondition(String condition,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,memberId,recommendIds);
    }

    /**
    * 删除签到
    */
    public void deleteCheckIn(Long checkInId) {
        CheckIn entity=this.baseMapper.selectById(checkInId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加签到
    */
    public void addCheckIn(CheckIn checkIn) {
        this.baseMapper.insert(checkIn);
    }
}
