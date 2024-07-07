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
import cn.stylefeng.guns.modular.fin.service.TeamEarningService;
import cn.stylefeng.guns.modular.fin.wrapper.TeamEarningWrapper;
import cn.stylefeng.guns.modular.fin.entity.TeamEarning;
import cn.stylefeng.guns.modular.fin.constant.TeamEarningMap;

/**
 * 团队佣金控制器
 *
 * @author yaying.liu
 * @since 2022-02-18 18:53:54
 */
@Controller
@RequestMapping("/teamEarning")
public class TeamEarningController extends BaseController {

    private String PREFIX = "/modular/fin/teamEarning/";

     @Autowired
     private TeamEarningService teamEarningService;

    /**
     * 跳转到团队佣金首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "teamEarning.html";
    }

    /**
     * 跳转到添加团队佣金
     */
    @RequestMapping("/teamEarning_add")
    public String teamEarningAdd() {
        return PREFIX + "teamEarning_add.html";
    }

    /**
     * 跳转到修改团队佣金
     */
    @RequestMapping("/teamEarning_edit")
    public String teamEarningEdit(Long teamEarningId, Model model) {
        TeamEarning condition = this.teamEarningService.getById(teamEarningId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "teamEarning_edit.html";
    }

    /**
     * 团队佣金详情
     */
    @RequestMapping(value = "/detail/{teamEarningId}")
    @ResponseBody
    public Object detail(@PathVariable("teamEarningId") Long teamEarningId) {
        TeamEarning entity = teamEarningService.getById(teamEarningId);
      //  TeamEarningDto conditionDto = new TeamEarningDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询团队佣金列表
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
        Page<Map<String, Object>> result = teamEarningService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new TeamEarningWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑团队佣金
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "teamEarningId", dict = TeamEarningMap.class)
    @ResponseBody
    public ResponseData edit(TeamEarning teamEarning) {
        teamEarningService.updateById(teamEarning);
        return SUCCESS_TIP;
    }

    /**
     * 添加团队佣金
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加团队佣金", key = "name", dict = TeamEarningMap.class)
    @ResponseBody
    public ResponseData add(TeamEarning teamEarning, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (teamEarning == null) {
            return ResponseData.error("参数不能为空");
        }
        this.teamEarningService.addTeamEarning(teamEarning);
        return SUCCESS_TIP;
    }

    /**
     * 删除团队佣金
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除团队佣金", key = "teamEarningId", dict = TeamEarningMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long teamEarningId) {
        if (ToolUtil.isEmpty(teamEarningId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.teamEarningService.deleteTeamEarning(teamEarningId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{teamEarningId}")
    @ResponseBody
    public Object content(@PathVariable("teamEarningId") Long id) {
        TeamEarning teamEarning = teamEarningService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(teamEarning);
        return super.warpObject(new TeamEarningWrapper(stringObjectMap));
    }
}