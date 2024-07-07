package cn.stylefeng.guns.modular.promotion.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.promotion.entity.Declares;
import cn.stylefeng.guns.modular.promotion.mapper.DeclaresMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户协议Service
 *
 * @author yaying.liu
 * @Date 2019-10-12 19:31:14
 */
@Service
public  class DeclaresService extends ServiceImpl<DeclaresMapper,Declares>{

    /**
    * 查询用户协议
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除用户协议
    */
    public void deleteDeclares(Long declaresId) {
        Declares entity=this.baseMapper.selectById(declaresId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加用户协议
    */
    public void addDeclares(Declares declares) {
        this.baseMapper.insert(declares);
    }
}