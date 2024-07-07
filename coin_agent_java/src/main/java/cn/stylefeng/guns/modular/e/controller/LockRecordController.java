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
import cn.stylefeng.guns.modular.e.service.LockRecordService;
import cn.stylefeng.guns.modular.e.wrapper.LockRecordWrapper;
import cn.stylefeng.guns.modular.e.entity.LockRecord;
import cn.stylefeng.guns.modular.e.constant.LockRecordMap;

/**
 * 锁仓记录控制器
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:58
 */
@Controller
@RequestMapping("/lockRecord")
public class LockRecordController extends BaseController {

    private String PREFIX = "/modular/e/lockRecord/";

     @Autowired
     private LockRecordService lockRecordService;

    /**
     * 跳转到锁仓记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lockRecord.html";
    }

    /**
     * 跳转到添加锁仓记录
     */
    @RequestMapping("/lockRecord_add")
    public String lockRecordAdd() {
        return PREFIX + "lockRecord_add.html";
    }

    /**
     * 跳转到修改锁仓记录
     */
    @RequestMapping("/lockRecord_edit")
    public String lockRecordEdit(Long lockRecordId, Model model) {
        LockRecord condition = this.lockRecordService.getById(lockRecordId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "lockRecord_edit.html";
    }

    /**
     * 锁仓记录详情
     */
    @RequestMapping(value = "/detail/{lockRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("lockRecordId") Long lockRecordId) {
        LockRecord entity = lockRecordService.getById(lockRecordId);
      //  LockRecordDto conditionDto = new LockRecordDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询锁仓记录列表
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
        Page<Map<String, Object>> result = lockRecordService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new LockRecordWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑锁仓记录
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "lockRecordId", dict = LockRecordMap.class)
    @ResponseBody
    public ResponseData edit(LockRecord lockRecord) {
        lockRecordService.updateById(lockRecord);
        return SUCCESS_TIP;
    }

    /**
     * 添加锁仓记录
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加锁仓记录", key = "name", dict = LockRecordMap.class)
    @ResponseBody
    public ResponseData add(LockRecord lockRecord, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (lockRecord == null) {
            return ResponseData.error("参数不能为空");
        }
        this.lockRecordService.addLockRecord(lockRecord);
        return SUCCESS_TIP;
    }

    /**
     * 删除锁仓记录
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除锁仓记录", key = "lockRecordId", dict = LockRecordMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long lockRecordId) {
        if (ToolUtil.isEmpty(lockRecordId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.lockRecordService.deleteLockRecord(lockRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{lockRecordId}")
    @ResponseBody
    public Object content(@PathVariable("lockRecordId") Long id) {
        LockRecord lockRecord = lockRecordService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(lockRecord);
        return super.warpObject(new LockRecordWrapper(stringObjectMap));
    }
}