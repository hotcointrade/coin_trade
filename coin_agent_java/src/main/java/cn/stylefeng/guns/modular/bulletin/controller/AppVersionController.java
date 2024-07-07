package cn.stylefeng.guns.modular.bulletin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.bulletin.constant.AppVersionMap;
import cn.stylefeng.guns.modular.bulletin.entity.AppVersion;
import cn.stylefeng.guns.modular.bulletin.model.AppVersionDto;
import cn.stylefeng.guns.modular.bulletin.service.AppVersionService;
import cn.stylefeng.guns.modular.bulletin.wrapper.AppVersionWrapper;
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
 * 版本号控制器
 *
 * @author yaying.liu
 * @Date 2019-09-04 18:44:14
 */
@Controller
@RequestMapping("/appVersion")
public class AppVersionController extends BaseController {

    private String PREFIX = "/modular/bulletin/appVersion/";

    @Autowired
    private AppVersionService appVersionService;

    /**
     * 跳转到版本号首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appVersion.html";
    }

    /**
     * 跳转到添加版本号
     */
    @RequestMapping("/appVersion_add")
    public String appVersionAdd() {
        return PREFIX + "appVersion_add.html";
    }

    /**
     * 跳转到修改版本号
     */
    @RequestMapping("/appVersion_edit")
    public String appVersionEdit(Long appVersionId, Model model) {
        AppVersion condition = this.appVersionService.getById(appVersionId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "appVersion_edit.html";
    }

    /**
     * 版本号详情
     */
    @RequestMapping(value = "/detail/{appVersionId}")
    @ResponseBody
    public Object detail(@PathVariable("appVersionId") Long appVersionId) {
        AppVersion entity = appVersionService.getById(appVersionId);
        AppVersionDto conditionDto = new AppVersionDto();
        BeanUtil.copyProperties(entity, conditionDto);
        return conditionDto;
    }


    /**
     * 查询版本号列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = appVersionService.selectByCondition(condition);
        Page wrapped = new AppVersionWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑版本号
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "appVersionId", dict = AppVersionMap.class)
    @ResponseBody
    public ResponseData edit(AppVersion appVersion) {
        appVersionService.updateById(appVersion);
        return SUCCESS_TIP;
    }

    /**
     * 添加版本号
     */
    @RequestMapping("/add")
//    @BussinessLog(value = "添加版本号", key = "name", dict = AppVersionMap.class)
    @ResponseBody
    public ResponseData add(AppVersion appVersion, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (appVersion == null) {
            return ResponseData.error("参数不能为空");
        }
        this.appVersionService.addAppVersion(appVersion);
        return SUCCESS_TIP;
    }

    /**
     * 删除版本号
     */
    @RequestMapping("/delete")
//    @BussinessLog(value = "删除版本号", key = "appVersionId", dict = AppVersionMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long appVersionId) {
        if (ToolUtil.isEmpty(appVersionId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.appVersionService.deleteAppVersion(appVersionId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{appVersionId}")
    @ResponseBody
    public Object content(@PathVariable("appVersionId") Long id) {
        AppVersion appVersion = appVersionService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(appVersion);
        return super.warpObject(new AppVersionWrapper(stringObjectMap));
    }
}