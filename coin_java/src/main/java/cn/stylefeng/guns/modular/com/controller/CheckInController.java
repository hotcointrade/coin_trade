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
import cn.stylefeng.guns.modular.com.service.CheckInService;
import cn.stylefeng.guns.modular.com.wrapper.CheckInWrapper;
import cn.stylefeng.guns.modular.com.entity.CheckIn;
import cn.stylefeng.guns.modular.com.constant.CheckInMap;

/**
 * 签到控制器
 *
 * @author yaying.liu
 * @Date 2019-12-09 16:28:04
 */
@Controller
@RequestMapping("/checkIn")
public class CheckInController extends BaseController {

    private String PREFIX = "/modular/com/checkIn/";

     @Autowired
     private CheckInService checkInService;

    /**
     * 跳转到签到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "checkIn.html";
    }

    /**
     * 跳转到添加签到
     */
    @RequestMapping("/checkIn_add")
    public String checkInAdd() {
        return PREFIX + "checkIn_add.html";
    }

    /**
     * 跳转到修改签到
     */
    @RequestMapping("/checkIn_edit")
    public String checkInEdit(Long checkInId, Model model) {
        CheckIn condition = this.checkInService.getById(checkInId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "checkIn_edit.html";
    }

    /**
     * 签到详情
     */
    @RequestMapping(value = "/detail/{checkInId}")
    @ResponseBody
    public Object detail(@PathVariable("checkInId") Long checkInId) {
        CheckIn entity = checkInService.getById(checkInId);
      //  CheckInDto conditionDto = new CheckInDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询签到列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = checkInService.selectByCondition(condition);
        Page wrapped = new CheckInWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑签到
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "checkInId", dict = CheckInMap.class)
    @ResponseBody
    public ResponseData edit(CheckIn checkIn) {
        checkInService.updateById(checkIn);
        return SUCCESS_TIP;
    }

    /**
     * 添加签到
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加签到", key = "name", dict = CheckInMap.class)
    @ResponseBody
    public ResponseData add(CheckIn checkIn, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (checkIn == null) {
            return ResponseData.error("参数不能为空");
        }
        this.checkInService.addCheckIn(checkIn);
        return SUCCESS_TIP;
    }

    /**
     * 删除签到
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除签到", key = "checkInId", dict = CheckInMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long checkInId) {
        if (ToolUtil.isEmpty(checkInId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.checkInService.deleteCheckIn(checkInId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{checkInId}")
    @ResponseBody
    public Object content(@PathVariable("checkInId") Long id) {
        CheckIn checkIn = checkInService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(checkIn);
        return super.warpObject(new CheckInWrapper(stringObjectMap));
    }
}