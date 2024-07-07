package cn.stylefeng.guns.modular.meta_data.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.meta_data.constant.ConfigMap;
import cn.stylefeng.guns.modular.meta_data.constant.factory.MetaDataFactory;
import cn.stylefeng.guns.modular.meta_data.entity.Config;
import cn.stylefeng.guns.modular.meta_data.model.ConfigDto;
import cn.stylefeng.guns.modular.meta_data.service.ConfigService;
import cn.stylefeng.guns.modular.meta_data.wrapper.ConfigWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
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
 * 配置参数控制器
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

    private static String PREFIX = "/modular/meta_data/config/";

    @Autowired
    private ConfigService configService;

    /**
     * 跳转到管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "config.html";
    }


    /**
     * 跳转到添加
     */
    @RequestMapping("/config_add")
    public String configAdd() {
        return PREFIX + "config_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/config_edit")
    public String configEdit(Long configId, Model model) {
        Config condition = this.configService.getById(configId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX+"config_edit.html";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{configId}")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable("configId") Long configId) {
        Config config = configService.getById(configId);
        ConfigDto conditionDto = new ConfigDto();
        BeanUtil.copyProperties(config, conditionDto);
        return conditionDto;
    }

    /**
     * 查询列表
     */
    @ApiOperation("配置列表")
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {

        //根据条件查询
        Page<Map<String, Object>> result = configService.selectByCondition(condition);
        Page wrapped = new ConfigWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 公共参数列表
     * @param condition
     * @return
     */
    @RequestMapping("/comList")
    @ResponseBody
    public Object comList(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = configService.selectByConditionCommon(condition);
        Page wrapped = new ConfigWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }



    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "configId", dict = ConfigMap.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData edit(Config config) {
        configService.updateById(config);
        return SUCCESS_TIP;
    }


    /**
     * 添加参数
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加参数", key = "name", dict = ConfigMap.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData add(Config config, BindingResult result) {

        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (config == null) {
            return ResponseData.error("参数不能为空");
        }
        this.configService.addConfig(config);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除参数", key = "configId", dict = ConfigMap.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData delete(@RequestParam Long configId) {
        if (ToolUtil.isEmpty(configId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.configService.deleteConfig(configId);
        return SUCCESS_TIP;
    }

}
