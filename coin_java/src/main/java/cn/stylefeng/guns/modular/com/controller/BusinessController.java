package cn.stylefeng.guns.modular.com.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.com.service.BusinessService;
import cn.stylefeng.guns.modular.com.wrapper.BusinessWrapper;
import cn.stylefeng.guns.modular.com.entity.Business;
import cn.stylefeng.guns.modular.com.constant.BusinessMap;

/**
 * 行业资讯控制器
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:09:15
 */
@Controller
@RequestMapping("/business")
public class BusinessController extends BaseController {

    private String PREFIX = "/modular/com/business/";

     @Autowired
     private BusinessService businessService;

    /**
     * 跳转到行业资讯首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "business.html";
    }

    /**
     * 跳转到添加行业资讯
     */
    @RequestMapping("/business_add")
    public String businessAdd() {
        return PREFIX + "business_add.html";
    }

    /**
     * 跳转到修改行业资讯
     */
    @RequestMapping("/business_edit")
    public String businessEdit(Long businessId, Model model) {
        Business condition = this.businessService.getById(businessId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "business_edit.html";
    }

    /**
     * 行业资讯详情
     */
    @RequestMapping(value = "/detail/{businessId}")
    @ResponseBody
    public Object detail(@PathVariable("businessId") Long businessId) {
        Business entity = businessService.getById(businessId);
      //  BusinessDto conditionDto = new BusinessDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询行业资讯列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = businessService.selectByCondition(condition);
        Page wrapped = new BusinessWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑行业资讯
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "businessId", dict = BusinessMap.class)
    @ResponseBody
    public ResponseData edit(Business business) {
        businessService.edit(business);
//        businessService.updateById(business);
        return SUCCESS_TIP;
    }

    /**
     * 添加行业资讯
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加行业资讯", key = "name", dict = BusinessMap.class)
    @ResponseBody
    public ResponseData add(Business business, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (business == null) {
            return ResponseData.error("参数不能为空");
        }
        this.businessService.addBusiness(business);
        return SUCCESS_TIP;
    }

    /**
     * 删除行业资讯
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除行业资讯", key = "businessId", dict = BusinessMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long businessId) {
        if (ToolUtil.isEmpty(businessId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.businessService.deleteBusiness(businessId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{businessId}")
    @ResponseBody
    public Object content(@PathVariable("businessId") Long id) {
        Business business = businessService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(business);
        return super.warpObject(new BusinessWrapper(stringObjectMap));
    }
}