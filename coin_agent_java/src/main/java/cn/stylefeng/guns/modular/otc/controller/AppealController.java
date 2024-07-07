package cn.stylefeng.guns.modular.otc.controller;

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
import cn.stylefeng.guns.modular.otc.service.AppealService;
import cn.stylefeng.guns.modular.otc.wrapper.AppealWrapper;
import cn.stylefeng.guns.modular.otc.entity.Appeal;
import cn.stylefeng.guns.modular.otc.constant.AppealMap;

/**
 * 申诉订单控制器
 *
 * @author yaying.liu
 * @since 2020-07-15 17:16:25
 */
@Controller
@RequestMapping("/appeal")
public class AppealController extends BaseController {

    private String PREFIX = "/modular/otc/appeal/";

     @Autowired
     private AppealService appealService;

    /**
     * 跳转到申诉订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appeal.html";
    }

    /**
     * 跳转到添加申诉订单
     */
    @RequestMapping("/appeal_add")
    public String appealAdd() {
        return PREFIX + "appeal_add.html";
    }

    /**
     * 跳转到修改申诉订单
     */
    @RequestMapping("/appeal_edit")
    public String appealEdit(Long appealId, Model model) {
        Appeal condition = this.appealService.getById(appealId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "appeal_edit.html";
    }

    /**
     * 申诉订单详情
     */
    @RequestMapping(value = "/detail/{appealId}")
    @ResponseBody
    public Object detail(@PathVariable("appealId") Long appealId) {
        Appeal entity = appealService.getById(appealId);
      //  AppealDto conditionDto = new AppealDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询申诉订单列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String timeLimit) {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> result = appealService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new AppealWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑申诉订单
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "appealId", dict = AppealMap.class)
    @ResponseBody
    public ResponseData edit(Appeal appeal) {
        appealService.updateById(appeal);
        return SUCCESS_TIP;
    }

    /**
     * 添加申诉订单
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加申诉订单", key = "name", dict = AppealMap.class)
    @ResponseBody
    public ResponseData add(Appeal appeal, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (appeal == null) {
            return ResponseData.error("参数不能为空");
        }
        this.appealService.addAppeal(appeal);
        return SUCCESS_TIP;
    }

    /**
     * 删除申诉订单
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除申诉订单", key = "appealId", dict = AppealMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long appealId) {
        if (ToolUtil.isEmpty(appealId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.appealService.deleteAppeal(appealId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{appealId}")
    @ResponseBody
    public Object content(@PathVariable("appealId") Long id) {
        Appeal appeal = appealService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(appeal);
        return super.warpObject(new AppealWrapper(stringObjectMap));
    }
}