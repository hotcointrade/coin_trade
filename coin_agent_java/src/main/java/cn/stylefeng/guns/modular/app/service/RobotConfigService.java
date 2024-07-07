package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.app.entity.Payment;
import cn.stylefeng.guns.modular.app.entity.RobotConfig;
import cn.stylefeng.guns.modular.app.mapper.PaymentMapper;
import cn.stylefeng.guns.modular.app.mapper.RobotMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public  class RobotConfigService extends ServiceImpl<RobotMapper, RobotConfig>{



    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }



    public void addRobotConfig(RobotConfig robotConfig) {
        this.baseMapper.insert(robotConfig);
    }
}