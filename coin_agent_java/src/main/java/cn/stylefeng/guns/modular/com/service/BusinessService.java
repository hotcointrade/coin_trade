package cn.stylefeng.guns.modular.com.service;

import cn.hutool.http.HtmlUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Business;
import cn.stylefeng.guns.modular.com.mapper.BusinessMapper;
/**
 * 行业资讯Service
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:09:15
 */
@Service
public  class BusinessService extends ServiceImpl<BusinessMapper,Business>{

    /**
    * 查询行业资讯
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除行业资讯
    */
    public void deleteBusiness(Long businessId) {
        Business entity=this.baseMapper.selectById(businessId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加行业资讯
    */
    public void addBusiness(Business business) {

//        Business entity= this.getById(business.getBusinessId());
        //对传入数据进行反转义处理
        String tempContent = business.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        business.setContent(tempContent);
        this.baseMapper.insert(business);
    }

    public void edit(Business business)
    {
        Business entity= this.getById(business.getBusinessId());
        //对传入数据进行反转义处理
        String tempContent = business.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        business.setContent(tempContent);
//        entity.setRemark(business.getRemark()).setTi
        this.updateById(business);
    }
}