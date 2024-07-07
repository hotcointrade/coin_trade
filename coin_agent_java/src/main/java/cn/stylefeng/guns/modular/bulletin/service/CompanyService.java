package cn.stylefeng.guns.modular.bulletin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.Company;
import cn.stylefeng.guns.modular.bulletin.mapper.CompanyMapper;
import cn.stylefeng.guns.modular.bulletin.model.CompanyDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CompanyService extends ServiceImpl<CompanyMapper, Company> {
    /**
     * 获取开户行信息
     * @return
//     */
//    public Result companyInfo() {
//        Company query = new Company();
//        query.setCompanyCode(CommonParam.COMPANY_CODE);
//        Company company = getOne(new QueryWrapper<>(query));
//        if(company==null)
//        {
//            return Result.fail(StatusCode.NOT_FOUND,"暂无开户信息,联系管理员");
//        }
//        CompanyDto dto = new CompanyDto();
//        BeanUtil.copyProperties(company, dto);
//        return Result.success("返回结果", StatusCode.SUCCESS, dto);
//    }

    /**
     * 查询平台账户
     */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
     * 删除平台账户
     */
    public void deleteCompany(Long companyId) {
        Company entity=this.baseMapper.selectById(companyId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加平台账户
     */
    public void addCompany(Company company) {
        this.baseMapper.insert(company);
    }



}
