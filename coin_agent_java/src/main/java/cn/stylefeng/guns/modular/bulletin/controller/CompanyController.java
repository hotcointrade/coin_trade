package cn.stylefeng.guns.modular.bulletin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.bulletin.constant.CompanyMap;
import cn.stylefeng.guns.modular.bulletin.entity.Company;
import cn.stylefeng.guns.modular.bulletin.model.CompanyBackDto;
import cn.stylefeng.guns.modular.bulletin.service.CompanyService;
import cn.stylefeng.guns.modular.bulletin.wrapper.CompanyWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 平台账户控制器
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:52:51
 */
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    private String PREFIX = "/modular/bulletin/company/";

    @Autowired
    private CompanyService companyService;

    /**
     * 跳转到平台账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "company.html";
    }

    /**
     * 跳转到添加平台账户
     */
    @RequestMapping("/company_add")
    public String companyAdd() {
        return PREFIX + "company_add.html";
    }

    /**
     * 跳转到修改平台账户
     */
    @RequestMapping("/company_edit")
    public String companyEdit(Long companyId, Model model) {
        Company condition = this.companyService.getById(companyId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "company_edit.html";
    }

    /**
     * 平台账户详情
     */
    @RequestMapping(value = "/detail/{companyId}")
    @ResponseBody
    public Object detail(@PathVariable("companyId") Long companyId) {
        Company entity = companyService.getById(companyId);
        CompanyBackDto conditionDto = new CompanyBackDto();
        BeanUtil.copyProperties(entity, conditionDto);
        return conditionDto;
    }


    /**
     * 查询平台账户列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = companyService.selectByCondition(condition);
        Page wrapped = new CompanyWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑平台账户
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(Company company) {
        companyService.updateById(company);
        return SUCCESS_TIP;
    }

    /**
     * 添加平台账户
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加平台账户", key = "name", dict = CompanyMap.class)
    @ResponseBody
    public ResponseData add(Company company, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (company == null) {
            return ResponseData.error("参数不能为空");
        }
        this.companyService.addCompany(company);
        return SUCCESS_TIP;
    }

    /**
     * 删除平台账户
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除平台账户", key = "companyId", dict = CompanyMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long companyId) {
        if (ToolUtil.isEmpty(companyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.companyService.deleteCompany(companyId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{companyId}")
    @ResponseBody
    public Object content(@PathVariable("companyId") Long id) {
        Company company = companyService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(company);
        return super.warpObject(new CompanyWrapper(stringObjectMap));
    }
}