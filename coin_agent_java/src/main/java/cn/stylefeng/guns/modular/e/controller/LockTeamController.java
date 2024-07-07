package cn.stylefeng.guns.modular.e.controller;

import cn.stylefeng.guns.modular.e.entity.LockAuto;
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
import java.util.List;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.e.service.LockTeamService;
import cn.stylefeng.guns.modular.e.wrapper.LockTeamWrapper;
import cn.stylefeng.guns.modular.e.entity.LockTeam;
import cn.stylefeng.guns.modular.e.constant.LockTeamMap;

/**
 * 锁仓团队奖配置控制器
 *
 * @author yaying.liu
 * @since 2020-09-25 10:45:37
 */
@Controller
@RequestMapping("/lockTeam")
public class LockTeamController extends BaseController {

    private String PREFIX = "/modular/e/lockTeam/";

     @Autowired
     private LockTeamService lockTeamService;

    /**
     * 跳转到锁仓团队奖配置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lockTeam.html";
    }

    /**
     * 跳转到添加锁仓团队奖配置
     */
    @RequestMapping("/lockTeam_add")
    public String lockTeamAdd() {
        return PREFIX + "lockTeam_add.html";
    }

    /**
     * 跳转到修改锁仓团队奖配置
     */
    @RequestMapping("/lockTeam_edit")
    public String lockTeamEdit(Long lockTeamId, Model model) {
        LockTeam condition = this.lockTeamService.getById(lockTeamId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "lockTeam_edit.html";
    }

    /**
     * 锁仓团队奖配置详情
     */
    @RequestMapping(value = "/detail/{lockTeamId}")
    @ResponseBody
    public Object detail(@PathVariable("lockTeamId") Long lockTeamId) {
        LockTeam entity = lockTeamService.getById(lockTeamId);
      //  LockTeamDto conditionDto = new LockTeamDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询锁仓团队奖配置列表
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
        Page<Map<String, Object>> result = lockTeamService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new LockTeamWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑锁仓团队奖配置
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "lockTeamId", dict = LockTeamMap.class)
    @ResponseBody
    public ResponseData edit(LockTeam lockTeam) {

        BigDecimal value=BigDecimal.ZERO;
        List<LockTeam> list = lockTeamService.list();
        for (LockTeam auto : list) {
            if(auto.getLockTeamId().intValue()==lockTeam.getLockTeamId().intValue())
            {
                auto.setPrice(lockTeam.getPrice());
            }
            if(value.compareTo(auto.getPrice())>=0)
            {
                throw new ServiceException(400,"错误，值范围不符合由小到大排列");
//                return ResponseData.error("错误，值范围不符合由小到大排列");
            }
            value=auto.getPrice();
        }
        lockTeamService.updateById(lockTeam);
        return SUCCESS_TIP;
    }

    /**
     * 添加锁仓团队奖配置
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加锁仓团队奖配置", key = "name", dict = LockTeamMap.class)
    @ResponseBody
    public ResponseData add(LockTeam lockTeam, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (lockTeam == null) {
            return ResponseData.error("参数不能为空");
        }
        this.lockTeamService.addLockTeam(lockTeam);
        return SUCCESS_TIP;
    }

    /**
     * 删除锁仓团队奖配置
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除锁仓团队奖配置", key = "lockTeamId", dict = LockTeamMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long lockTeamId) {
        if (ToolUtil.isEmpty(lockTeamId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.lockTeamService.deleteLockTeam(lockTeamId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{lockTeamId}")
    @ResponseBody
    public Object content(@PathVariable("lockTeamId") Long id) {
        LockTeam lockTeam = lockTeamService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(lockTeam);
        return super.warpObject(new LockTeamWrapper(stringObjectMap));
    }
}