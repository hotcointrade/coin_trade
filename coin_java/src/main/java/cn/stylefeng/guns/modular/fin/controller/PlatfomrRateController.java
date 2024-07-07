package cn.stylefeng.guns.modular.fin.controller;

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
import cn.stylefeng.guns.modular.fin.service.PlatfomrRateService;
import cn.stylefeng.guns.modular.fin.wrapper.PlatfomrRateWrapper;
import cn.stylefeng.guns.modular.fin.entity.PlatfomrRate;
import cn.stylefeng.guns.modular.fin.constant.PlatfomrRateMap;

/**
 * 系统汇率控制器
 *
 * @author yaying.liu
 * @since 2022-10-18 13:18:11
 */
@Controller
@RequestMapping("/platfomrRate")
public class PlatfomrRateController extends BaseController {

    private String PREFIX = "/modular/fin/platfomrRate/";

     @Autowired
     private PlatfomrRateService platfomrRateService;

    /**
     * 跳转到系统汇率首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "platfomrRate.html";
    }

    /**
     * 跳转到添加系统汇率
     */
    @RequestMapping("/platfomrRate_add")
    public String platfomrRateAdd() {
        return PREFIX + "platfomrRate_add.html";
    }

    /**
     * 跳转到修改系统汇率
     */
    @RequestMapping("/platfomrRate_edit")
    public String platfomrRateEdit(Long platformRateId, Model model) {
        PlatfomrRate condition = this.platfomrRateService.getById(platformRateId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "platfomrRate_edit.html";
    }

    /**
     * 系统汇率详情
     */
    @RequestMapping(value = "/detail/{platformRateId}")
    @ResponseBody
    public Object detail(@PathVariable("platformRateId") Long platformRateId) {
        PlatfomrRate entity = platfomrRateService.getById(platformRateId);
      //  PlatfomrRateDto conditionDto = new PlatfomrRateDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询系统汇率列表
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
        Page<Map<String, Object>> result = platfomrRateService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new PlatfomrRateWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑系统汇率
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "platformRateId", dict = PlatfomrRateMap.class)
    @ResponseBody
    public ResponseData edit(PlatfomrRate platfomrRate) {
        platfomrRateService.updateById(platfomrRate);
        return SUCCESS_TIP;
    }

    /**
     * 添加系统汇率
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加系统汇率", key = "name", dict = PlatfomrRateMap.class)
    @ResponseBody
    public ResponseData add(PlatfomrRate platfomrRate, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (platfomrRate == null) {
            return ResponseData.error("参数不能为空");
        }
        this.platfomrRateService.addPlatfomrRate(platfomrRate);
        return SUCCESS_TIP;
    }

    /**
     * 删除系统汇率
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除系统汇率", key = "platformRateId", dict = PlatfomrRateMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long platformRateId) {
        if (ToolUtil.isEmpty(platformRateId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.platfomrRateService.deletePlatfomrRate(platformRateId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{platformRateId}")
    @ResponseBody
    public Object content(@PathVariable("platformRateId") Long id) {
        PlatfomrRate platfomrRate = platfomrRateService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(platfomrRate);
        return super.warpObject(new PlatfomrRateWrapper(stringObjectMap));
    }
}
