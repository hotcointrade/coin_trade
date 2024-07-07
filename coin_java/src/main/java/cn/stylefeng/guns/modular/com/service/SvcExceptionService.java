package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.SvcException;
import cn.stylefeng.guns.modular.com.mapper.SvcExceptionMapper;
/**
 * 异常信息Service
 *
 * @author yaying.liu
 * @Date 2019-12-24 10:20:02
 */
@Service
public  class SvcExceptionService extends ServiceImpl<SvcExceptionMapper,SvcException>{

    /**
    * 查询异常信息
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除异常信息
    */
    public void deleteSvcException(Long svcExceptionId) {
        SvcException entity=this.baseMapper.selectById(svcExceptionId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加异常信息
    */
    public void addSvcException(SvcException svcException) {
        this.baseMapper.insert(svcException);
    }
}