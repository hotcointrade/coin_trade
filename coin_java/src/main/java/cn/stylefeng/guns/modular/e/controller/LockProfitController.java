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
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.e.service.LockProfitService;
import cn.stylefeng.guns.modular.e.wrapper.LockProfitWrapper;
import cn.stylefeng.guns.modular.e.entity.LockProfit;
import cn.stylefeng.guns.modular.e.constant.LockProfitMap;

/**
 * 锁仓静态收益记录控制器
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:24
 */
@Controller
@RequestMapping("/lockProfit")
public class LockProfitController extends BaseController {

    private String PREFIX = "/modular/e/lockProfit/";

     @Autowired
     private LockProfitService lockProfitService;

    /**
     * 跳转到锁仓静态收益记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lockProfit.html";
    }

    /**
     * 跳转到添加锁仓静态收益记录
     */
    @RequestMapping("/lockProfit_add")
    public String lockProfitAdd() {
        return PREFIX + "lockProfit_add.html";
    }

    /**
     * 跳转到修改锁仓静态收益记录
     */
    @RequestMapping("/lockProfit_edit")
    public String lockProfitEdit(Long lockProfitId, Model model) {
        LockProfit condition = this.lockProfitService.getById(lockProfitId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "lockProfit_edit.html";
    }

    /**
     * 锁仓静态收益记录详情
     */
    @RequestMapping(value = "/detail/{lockProfitId}")
    @ResponseBody
    public Object detail(@PathVariable("lockProfitId") Long lockProfitId) {
        LockProfit entity = lockProfitService.getById(lockProfitId);
      //  LockProfitDto conditionDto = new LockProfitDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询锁仓静态收益记录列表
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
        Page<Map<String, Object>> result = lockProfitService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new LockProfitWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑锁仓静态收益记录
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "lockProfitId", dict = LockProfitMap.class)
    @ResponseBody
    public ResponseData edit(LockProfit lockProfit) {
        lockProfitService.updateById(lockProfit);
        return SUCCESS_TIP;
    }

    /**
     * 添加锁仓静态收益记录
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加锁仓静态收益记录", key = "name", dict = LockProfitMap.class)
    @ResponseBody
    public ResponseData add(LockProfit lockProfit, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (lockProfit == null) {
            return ResponseData.error("参数不能为空");
        }
        this.lockProfitService.addLockProfit(lockProfit);
        return SUCCESS_TIP;
    }

    /**
     * 删除锁仓静态收益记录
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除锁仓静态收益记录", key = "lockProfitId", dict = LockProfitMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long lockProfitId) {
        if (ToolUtil.isEmpty(lockProfitId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.lockProfitService.deleteLockProfit(lockProfitId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{lockProfitId}")
    @ResponseBody
    public Object content(@PathVariable("lockProfitId") Long id) {
        LockProfit lockProfit = lockProfitService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(lockProfit);
        return super.warpObject(new LockProfitWrapper(stringObjectMap));
    }
}