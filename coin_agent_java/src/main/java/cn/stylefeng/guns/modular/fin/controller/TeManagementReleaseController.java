package cn.stylefeng.guns.modular.fin.controller;

import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
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
import cn.stylefeng.guns.modular.fin.entity.TeManagementRelease;
import cn.stylefeng.guns.modular.fin.service.TeManagementReleaseService;

@Controller
@RequestMapping("/teManagementRelease")
public class TeManagementReleaseController extends BaseController{

	private String PREFIX = "/modular/fin/te/";
    @Autowired
    private TeManagementReleaseService teManagementReleaseService;

    /**
     * 跳转到理财释放记录首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "teManagementRelease.html";
    }

	 /**
     * 跳转到添加理财释放记录
     */
    @RequestMapping("/teManagementRelease_add")
    public String spotAdd() {
        return PREFIX + "teManagementRelease_add.html";
    }
	 /**
     * 跳转到修改理财释放记录
     */
    @RequestMapping("/teManagementRelease_edit")
    public String spotEdit(Long id, Model model) {
        TeManagementRelease condition = this.teManagementReleaseService.getById(id);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "teManagementRelease_edit.html";
    }

	/**
     * 理财释放记录详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        TeManagementRelease entity = teManagementReleaseService.getById(id);
        return entity;
    }
	/**
     * 查询理财释放记录列表
     */

    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = teManagementReleaseService.selectByCondition(condition,memberId,recommendIds);
        return LayuiPageFactory.createPageInfo(result);
    }

	/**
     * 编辑理财释放记录
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(TeManagementRelease spot) {
        teManagementReleaseService.updateById(spot);
        return SUCCESS_TIP;
    }

	/**
     * 添加理财释放记录
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(TeManagementRelease teManagementRelease, BindingResult result) {

        //spot1.setDel("N");
        //int count = this.teManagementReleaseService.count(new QueryWrapper<>(teManagementRelease));
        //if (count >0){
        //    return ResponseData.error("该记录已存在");
        //}
        this.teManagementReleaseService.save(teManagementRelease);
        return SUCCESS_TIP;
    }

	/**
     * 删除理财释放记录
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //TeManagementRelease entity =  this.teManagementReleaseService.getById(id);

        teManagementReleaseService.removeById(id);
        return SUCCESS_TIP;
    }

}
