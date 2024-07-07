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
import cn.stylefeng.guns.modular.com.service.SvcExceptionService;
import cn.stylefeng.guns.modular.com.wrapper.SvcExceptionWrapper;
import cn.stylefeng.guns.modular.com.entity.SvcException;
import cn.stylefeng.guns.modular.com.constant.SvcExceptionMap;

/**
 * 异常信息控制器
 *
 * @author yaying.liu
 * @Date 2019-12-24 10:20:02
 */
@Controller
@RequestMapping("/svcException")
public class SvcExceptionController extends BaseController {

    private String PREFIX = "/modular/com/svcException/";

     @Autowired
     private SvcExceptionService svcExceptionService;

    /**
     * 跳转到异常信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "svcException.html";
    }

    /**
     * 跳转到添加异常信息
     */
    @RequestMapping("/svcException_add")
    public String svcExceptionAdd() {
        return PREFIX + "svcException_add.html";
    }

    /**
     * 跳转到修改异常信息
     */
    @RequestMapping("/svcException_edit")
    public String svcExceptionEdit(Long svcExceptionId, Model model) {
        SvcException condition = this.svcExceptionService.getById(svcExceptionId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "svcException_edit.html";
    }

    /**
     * 异常信息详情
     */
    @RequestMapping(value = "/detail/{svcExceptionId}")
    @ResponseBody
    public Object detail(@PathVariable("svcExceptionId") Long svcExceptionId) {
        SvcException entity = svcExceptionService.getById(svcExceptionId);
      //  SvcExceptionDto conditionDto = new SvcExceptionDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询异常信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = svcExceptionService.selectByCondition(condition);
        Page wrapped = new SvcExceptionWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑异常信息
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "svcExceptionId", dict = SvcExceptionMap.class)
    @ResponseBody
    public ResponseData edit(SvcException svcException) {
        svcExceptionService.updateById(svcException);
        return SUCCESS_TIP;
    }

    /**
     * 添加异常信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加异常信息", key = "name", dict = SvcExceptionMap.class)
    @ResponseBody
    public ResponseData add(SvcException svcException, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (svcException == null) {
            return ResponseData.error("参数不能为空");
        }
        this.svcExceptionService.addSvcException(svcException);
        return SUCCESS_TIP;
    }

    /**
     * 删除异常信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除异常信息", key = "svcExceptionId", dict = SvcExceptionMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long svcExceptionId) {
        if (ToolUtil.isEmpty(svcExceptionId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.svcExceptionService.deleteSvcException(svcExceptionId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{svcExceptionId}")
    @ResponseBody
    public Object content(@PathVariable("svcExceptionId") Long id) {
        SvcException svcException = svcExceptionService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(svcException);
        return super.warpObject(new SvcExceptionWrapper(stringObjectMap));
    }
}