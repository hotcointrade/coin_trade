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
import cn.stylefeng.guns.modular.app.service.TeamRewardsService;
import cn.stylefeng.guns.modular.app.wrapper.TeamRewardsWrapper;
import cn.stylefeng.guns.modular.app.entity.TeamRewards;
import cn.stylefeng.guns.modular.app.constant.TeamRewardsMap;

/**
 * 社区奖励控制器
 *
 * @author yaying.liu
 * @since 2022-07-07 17:45:57
 */
@Controller
@RequestMapping("/teamRewards")
public class TeamRewardsController extends BaseController {

    private String PREFIX = "/modular/app/teamRewards/";

     @Autowired
     private TeamRewardsService teamRewardsService;

    /**
     * 跳转到社区奖励首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "teamRewards.html";
    }

    /**
     * 跳转到添加社区奖励
     */
    @RequestMapping("/teamRewards_add")
    public String teamRewardsAdd() {
        return PREFIX + "teamRewards_add.html";
    }

    /**
     * 跳转到修改社区奖励
     */
    @RequestMapping("/teamRewards_edit")
    public String teamRewardsEdit(Long teamRewardsId, Model model) {
        TeamRewards condition = this.teamRewardsService.getById(teamRewardsId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "teamRewards_edit.html";
    }

    /**
     * 社区奖励详情
     */
    @RequestMapping(value = "/detail/{teamRewardsId}")
    @ResponseBody
    public Object detail(@PathVariable("teamRewardsId") Long teamRewardsId) {
        TeamRewards entity = teamRewardsService.getById(teamRewardsId);
      //  TeamRewardsDto conditionDto = new TeamRewardsDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询社区奖励列表
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
        Page<Map<String, Object>> result = teamRewardsService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new TeamRewardsWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑社区奖励
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "teamRewardsId", dict = TeamRewardsMap.class)
    @ResponseBody
    public ResponseData edit(TeamRewards teamRewards) {
        teamRewardsService.updateById(teamRewards);
        return SUCCESS_TIP;
    }

    /**
     * 添加社区奖励
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加社区奖励", key = "name", dict = TeamRewardsMap.class)
    @ResponseBody
    public ResponseData add(TeamRewards teamRewards, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (teamRewards == null) {
            return ResponseData.error("参数不能为空");
        }
        this.teamRewardsService.addTeamRewards(teamRewards);
        return SUCCESS_TIP;
    }

    /**
     * 删除社区奖励
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除社区奖励", key = "teamRewardsId", dict = TeamRewardsMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long teamRewardsId) {
        if (ToolUtil.isEmpty(teamRewardsId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.teamRewardsService.deleteTeamRewards(teamRewardsId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{teamRewardsId}")
    @ResponseBody
    public Object content(@PathVariable("teamRewardsId") Long id) {
        TeamRewards teamRewards = teamRewardsService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(teamRewards);
        return super.warpObject(new TeamRewardsWrapper(stringObjectMap));
    }
}