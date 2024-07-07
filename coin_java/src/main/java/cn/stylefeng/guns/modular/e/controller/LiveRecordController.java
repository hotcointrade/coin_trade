package cn.stylefeng.guns.modular.e.controller;

import cn.stylefeng.guns.core.common.constant.DefaultAvatar;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.shiro.ShiroUser;
import cn.stylefeng.guns.core.util.Base64Util;
import cn.stylefeng.guns.modular.system.entity.FileInfo;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
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

import java.io.IOException;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.e.service.LiveRecordService;
import cn.stylefeng.guns.modular.e.wrapper.LiveRecordWrapper;
import cn.stylefeng.guns.modular.e.entity.LiveRecord;
import cn.stylefeng.guns.modular.e.constant.LiveRecordMap;

import javax.servlet.http.HttpServletResponse;

/**
 * 生活支付打款控制器
 *
 * @author yaying.liu
 * @Date 2020-06-29 17:01:19
 */
@Controller
@RequestMapping("/liveRecord")
public class LiveRecordController extends BaseController {

    private String PREFIX = "/modular/e/liveRecord/";

     @Autowired
     private LiveRecordService liveRecordService;

    /**
     * 跳转到生活支付打款首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "liveRecord.html";
    }

    /**
     * 跳转到添加生活支付打款
     */
    @RequestMapping("/liveRecord_add")
    public String liveRecordAdd() {
        return PREFIX + "liveRecord_add.html";
    }

    /**
     * 跳转到修改生活支付打款
     */
    @RequestMapping("/liveRecord_edit")
    public String liveRecordEdit(Long liveRecordId, Model model) {
        LiveRecord condition = this.liveRecordService.getById(liveRecordId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "liveRecord_edit.html";
    }

    /**
     * 生活支付打款详情
     */
    @RequestMapping(value = "/detail/{liveRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("liveRecordId") Long liveRecordId) {
        LiveRecord entity = liveRecordService.getById(liveRecordId);
      //  LiveRecordDto conditionDto = new LiveRecordDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询生活支付打款列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition
                    ,@RequestParam(required = false) String orderNo
                    ,@RequestParam(required = false) String status
    ) {
        //根据条件查询
        Page<Map<String, Object>> result = liveRecordService.selectByCondition(condition,orderNo,status);
        Page wrapped = new LiveRecordWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑生活支付打款
     */
    @RequestMapping("/edit")
   // @BussinessLog(value = "编辑参数", key = "liveRecordId", dict = LiveRecordMap.class)
    @ResponseBody
    public ResponseData edit(LiveRecord liveRecord) {
        liveRecordService.updateById(liveRecord);
        return SUCCESS_TIP;
    }

    /**
     * 添加生活支付打款
     */
    @RequestMapping("/add")
   // @BussinessLog(value = "添加生活支付打款", key = "name", dict = LiveRecordMap.class)
    @ResponseBody
    public ResponseData add(LiveRecord liveRecord, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (liveRecord == null) {
            return ResponseData.error("参数不能为空");
        }
        this.liveRecordService.addLiveRecord(liveRecord);
        return SUCCESS_TIP;
    }

    /**
     * 删除生活支付打款
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除生活支付打款", key = "liveRecordId", dict = LiveRecordMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long liveRecordId) {
        if (ToolUtil.isEmpty(liveRecordId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.liveRecordService.deleteLiveRecord(liveRecordId);
        return SUCCESS_TIP;
    }


     /**
     * 已完成打款
     */
    @RequestMapping("/finish")
    @ResponseBody
    public ResponseData finish(@RequestParam Long liveRecordId) {
        if (ToolUtil.isEmpty(liveRecordId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.liveRecordService.finish(liveRecordId);
        return SUCCESS_TIP;
    }





    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{liveRecordId}")
    @ResponseBody
    public Object content(@PathVariable("liveRecordId") Long id) {
        LiveRecord liveRecord = liveRecordService.getById(id);
//        liveRecord.setLinkCode(new String(Base64Util.decode(liveRecord.getLinkCode())));
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(liveRecord);
        return super.warpObject(new LiveRecordWrapper(stringObjectMap));
    }
}