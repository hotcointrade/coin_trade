package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.PhoneCode;
import cn.stylefeng.guns.modular.com.mapper.PhoneCodeMapper;
/**
 * 电话区号Service
 *
 * @author yaying.liu
 * @Date 2019-12-20 17:59:48
 */
@Service
public  class PhoneCodeService extends ServiceImpl<PhoneCodeMapper,PhoneCode>{

    /**
    * 查询电话区号
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除电话区号
    */
    public void deletePhoneCode(Long phoneCodeId) {
        PhoneCode entity=this.baseMapper.selectById(phoneCodeId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加电话区号
    */
    public void addPhoneCode(PhoneCode phoneCode) {
        this.baseMapper.insert(phoneCode);
    }
}