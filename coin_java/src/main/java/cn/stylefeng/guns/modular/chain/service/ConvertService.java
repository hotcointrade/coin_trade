package cn.stylefeng.guns.modular.chain.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Convert;
import cn.stylefeng.guns.modular.chain.mapper.ConvertMapper;
/**
 * 豆兑换信息Service
 *
 * @author yaying.liu
 * @Date 2020-01-17 14:46:22
 */
@Service
public  class ConvertService extends ServiceImpl<ConvertMapper,Convert>{

    /**
    * 查询豆兑换信息
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除豆兑换信息
    */
    public void deleteConvert(Long convertId) {
        Convert entity=this.baseMapper.selectById(convertId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加豆兑换信息
    */
    public void addConvert(Convert convert) {
        this.baseMapper.insert(convert);
    }
}