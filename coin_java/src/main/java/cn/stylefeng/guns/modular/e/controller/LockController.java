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
import cn.stylefeng.guns.modular.e.service.LockService;
import cn.stylefeng.guns.modular.e.wrapper.LockWrapper;
import cn.stylefeng.guns.modular.e.entity.Lock;
import cn.stylefeng.guns.modular.e.constant.LockMap;

/**
 * 锁仓合约配置控制器
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:07
 */
@Controller
@RequestMapping("/lock")
public class LockController extends BaseController {

    private String PREFIX = "/modular/e/lock/";

     @Autowired
     private LockService lockService;

    /**
     * 跳转到锁仓合约配置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lock.html";
    }

    /**
     * 跳转到添加锁仓合约配置
     */
    @RequestMapping("/lock_add")
    public String lockAdd() {
        return PREFIX + "lock_add.html";
    }

    /**
     * 跳转到修改锁仓合约配置
     */
    @RequestMapping("/lock_edit")
    public String lockEdit(Long lockId, Model model) {
        Lock condition = this.lockService.getById(lockId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "lock_edit.html";
    }

    /**
     * 锁仓合约配置详情
     */
    @RequestMapping(value = "/detail/{lockId}")
    @ResponseBody
    public Object detail(@PathVariable("lockId") Long lockId) {
        Lock entity = lockService.getById(lockId);
      //  LockDto conditionDto = new LockDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询锁仓合约配置列表
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
        Page<Map<String, Object>> result = lockService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new LockWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑锁仓合约配置
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "lockId", dict = LockMap.class)
    @ResponseBody
    public ResponseData edit(Lock lock) {
        lockService.updateById(lock);
        return SUCCESS_TIP;
    }

    /**
     * 添加锁仓合约配置
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加锁仓合约配置", key = "name", dict = LockMap.class)
    @ResponseBody
    public ResponseData add(Lock lock, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (lock == null) {
            return ResponseData.error("参数不能为空");
        }
        this.lockService.addLock(lock);
        return SUCCESS_TIP;
    }

    /**
     * 删除锁仓合约配置
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除锁仓合约配置", key = "lockId", dict = LockMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long lockId) {
        if (ToolUtil.isEmpty(lockId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.lockService.deleteLock(lockId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{lockId}")
    @ResponseBody
    public Object content(@PathVariable("lockId") Long id) {
        Lock lock = lockService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(lock);
        return super.warpObject(new LockWrapper(stringObjectMap));
    }
}