package cn.stylefeng.guns.modular.fin.controller;

import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.stylefeng.guns.modular.fin.entity.TeManagement;
import cn.stylefeng.guns.modular.fin.service.TeManagementService;

@Controller
@RequestMapping("/teManagement")
public class TeManagementController extends BaseController{

	private String PREFIX = "/modular/fin/te/";
    @Autowired
    private TeManagementService teManagementService;

    /**
     * 跳转到理财认购币首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "teManagement.html";
    }
	
	 /**
     * 跳转到添加理财认购币
     */
    @RequestMapping("/teManagement_add")
    public String spotAdd() {
        return PREFIX + "teManagement_add.html";
    }
	 /**
     * 跳转到修改理财认购币
     */
    @RequestMapping("/teManagement_edit")
    public String spotEdit(Long id, Model model) {
        TeManagement condition = this.teManagementService.getById(id);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "teManagement_edit.html";
    }
	
	/**
     * 理财认购币详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        TeManagement entity = teManagementService.getById(id);
        return entity;
    }
	/**
     * 查询理财认购币列表
     */
   
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        Page<Map<String, Object>> result = teManagementService.selectByCondition(condition);
        return LayuiPageFactory.createPageInfo(result);
    }
	
	/**
     * 编辑理财认购币
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(TeManagement spot) {
        teManagementService.updateById(spot);
        return SUCCESS_TIP;
    }
	
	/**
     * 添加理财认购币
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(TeManagement teManagement, BindingResult result) {
        
        //spot1.setDel("N");
        //int count = this.teManagementService.count(new QueryWrapper<>(teManagement));
        //if (count >0){
        //    return ResponseData.error("该记录已存在");
        //}
        this.teManagementService.save(teManagement);
        return SUCCESS_TIP;
    }
	
	/**
     * 删除理财认购币
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //TeManagement entity =  this.teManagementService.getById(id);
        
        teManagementService.removeById(id);
        return SUCCESS_TIP;
    }
    
}