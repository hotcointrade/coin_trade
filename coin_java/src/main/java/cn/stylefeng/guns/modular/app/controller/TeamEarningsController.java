package cn.stylefeng.guns.modular.app.controller;

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
import cn.stylefeng.guns.modular.app.service.TeamEarningsService;
import cn.stylefeng.guns.modular.app.wrapper.TeamEarningsWrapper;
import cn.stylefeng.guns.modular.app.entity.TeamEarnings;
import cn.stylefeng.guns.modular.app.constant.TeamEarningsMap;

/**
 * 团队收益控制器
 *
 * @author yaying.liu
 * @since 2022-02-18 20:22:22
 */
@Controller
@RequestMapping("/teamEarnings")
public class TeamEarningsController extends BaseController {

    private String PREFIX = "/modular/app/teamEarnings/";

     @Autowired
     private TeamEarningsService teamEarningsService;

    /**
     * 跳转到团队收益首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "teamEarnings.html";
    }

    /**
     * 跳转到添加团队收益
     */
    @RequestMapping("/teamEarnings_add")
    public String teamEarningsAdd() {
        return PREFIX + "teamEarnings_add.html";
    }

    /**
     * 跳转到修改团队收益
     */
    @RequestMapping("/teamEarnings_edit")
    public String teamEarningsEdit(Long teamEarningsId, Model model) {
        TeamEarnings condition = this.teamEarningsService.getById(teamEarningsId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "teamEarnings_edit.html";
    }

    /**
     * 团队收益详情
     */
    @RequestMapping(value = "/detail/{teamEarningsId}")
    @ResponseBody
    public Object detail(@PathVariable("teamEarningsId") Long teamEarningsId) {
        TeamEarnings entity = teamEarningsService.getById(teamEarningsId);
      //  TeamEarningsDto conditionDto = new TeamEarningsDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询团队收益列表
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
        Page<Map<String, Object>> result = teamEarningsService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new TeamEarningsWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑团队收益
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "teamEarningsId", dict = TeamEarningsMap.class)
    @ResponseBody
    public ResponseData edit(TeamEarnings teamEarnings) {
        teamEarningsService.updateById(teamEarnings);
        return SUCCESS_TIP;
    }

    /**
     * 添加团队收益
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加团队收益", key = "name", dict = TeamEarningsMap.class)
    @ResponseBody
    public ResponseData add(TeamEarnings teamEarnings, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (teamEarnings == null) {
            return ResponseData.error("参数不能为空");
        }
        this.teamEarningsService.addTeamEarnings(teamEarnings);
        return SUCCESS_TIP;
    }

    /**
     * 删除团队收益
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除团队收益", key = "teamEarningsId", dict = TeamEarningsMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long teamEarningsId) {
        if (ToolUtil.isEmpty(teamEarningsId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.teamEarningsService.deleteTeamEarnings(teamEarningsId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{teamEarningsId}")
    @ResponseBody
    public Object content(@PathVariable("teamEarningsId") Long id) {
        TeamEarnings teamEarnings = teamEarningsService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(teamEarnings);
        return super.warpObject(new TeamEarningsWrapper(stringObjectMap));
    }
}