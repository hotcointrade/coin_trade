package cn.stylefeng.guns.modular.e.controller;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.e.service.LockAutoService;
import cn.stylefeng.guns.modular.e.wrapper.LockAutoWrapper;
import cn.stylefeng.guns.modular.e.entity.LockAuto;
import cn.stylefeng.guns.modular.e.constant.LockAutoMap;

/**
 * 锁仓动态收益配置控制器
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:59
 */
@Controller
@RequestMapping("/lockAuto")
public class LockAutoController extends BaseController {

    private String PREFIX = "/modular/e/lockAuto/";

    @Autowired
    private LockAutoService lockAutoService;

    /**
     * 跳转到锁仓动态收益配置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lockAuto.html";
    }

    /**
     * 跳转到添加锁仓动态收益配置
     */
    @RequestMapping("/lockAuto_add")
    public String lockAutoAdd() {
        return PREFIX + "lockAuto_add.html";
    }

    /**
     * 跳转到修改锁仓动态收益配置
     */
    @RequestMapping("/lockAuto_edit")
    public String lockAutoEdit(Long lockAutoId, Model model) {
        LockAuto condition = this.lockAutoService.getById(lockAutoId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "lockAuto_edit.html";
    }

    /**
     * 锁仓动态收益配置详情
     */
    @RequestMapping(value = "/detail/{lockAutoId}")
    @ResponseBody
    public Object detail(@PathVariable("lockAutoId") Long lockAutoId) {
        LockAuto entity = lockAutoService.getById(lockAutoId);
        //  LockAutoDto conditionDto = new LockAutoDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询锁仓动态收益配置列表
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
        Page<Map<String, Object>> result = lockAutoService.selectByCondition(condition, beginTime, endTime);
        Page wrapped = new LockAutoWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑锁仓动态收益配置
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "lockAutoId", dict = LockAutoMap.class)
    @ResponseBody
    public ResponseData edit(LockAuto lockAuto) {

        BigDecimal value=BigDecimal.ZERO;
        List<LockAuto> list = lockAutoService.list();
        for (LockAuto auto : list) {
            if(auto.getLockAutoId().intValue()==lockAuto.getLockAutoId().intValue())
            {
                auto.setPrice(lockAuto.getPrice());
            }
            if(value.compareTo(auto.getPrice())>=0)
            {
                throw new ServiceException(400,"错误，值范围不符合由小到大排列");
//                return ResponseData.error("错误，值范围不符合由小到大排列");
            }
            value=auto.getPrice();
        }

        lockAutoService.updateById(lockAuto);
        return SUCCESS_TIP;
    }

    /**
     * 添加锁仓动态收益配置
     */
    @RequestMapping("/add")
    //  @BussinessLog(value = "添加锁仓动态收益配置", key = "name", dict = LockAutoMap.class)
    @ResponseBody
    public ResponseData add(LockAuto lockAuto, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (lockAuto == null) {
            return ResponseData.error("参数不能为空");
        }
        this.lockAutoService.addLockAuto(lockAuto);
        return SUCCESS_TIP;
    }

    /**
     * 删除锁仓动态收益配置
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除锁仓动态收益配置", key = "lockAutoId", dict = LockAutoMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long lockAutoId) {
        if (ToolUtil.isEmpty(lockAutoId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.lockAutoService.deleteLockAuto(lockAutoId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{lockAutoId}")
    @ResponseBody
    public Object content(@PathVariable("lockAutoId") Long id) {
        LockAuto lockAuto = lockAutoService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(lockAuto);
        return super.warpObject(new LockAutoWrapper(stringObjectMap));
    }
}