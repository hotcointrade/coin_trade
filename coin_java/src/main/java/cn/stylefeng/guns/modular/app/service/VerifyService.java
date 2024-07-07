package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Verify;
import cn.stylefeng.guns.modular.app.mapper.VerifyMapper;
/**
 * 实名认证Service
 *
 * @author yaying.liu
 * @Date 2020-03-11 16:31:58
 */
@Service
public  class VerifyService extends ServiceImpl<VerifyMapper,Verify>{

    /**
    * 查询实名认证
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除实名认证
    */
    public void deleteVerify(Long verifyId) {
        Verify entity=this.baseMapper.selectById(verifyId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加实名认证
    */
    public void addVerify(Verify verify) {
        this.baseMapper.insert(verify);
    }
}