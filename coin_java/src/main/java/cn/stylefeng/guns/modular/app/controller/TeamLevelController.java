package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import cn.stylefeng.guns.modular.app.service.TeamLevelService;
import cn.stylefeng.guns.modular.app.wrapper.TeamLevelWrapper;
import cn.stylefeng.guns.modular.app.entity.TeamLevel;
import cn.stylefeng.guns.modular.app.constant.TeamLevelMap;

/**
 * 团队返佣控制器
 *
 * @author yaying.liu
 * @since 2022-08-22 14:46:40
 */
@Controller
@RequestMapping("/teamLevel")
public class TeamLevelController extends BaseController {

    private String PREFIX = "/modular/app/teamLevel/";

     @Autowired
     private TeamLevelService teamLevelService;

    /**
     * 跳转到团队返佣首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "teamLevel.html";
    }

    /**
     * 跳转到添加团队返佣
     */
    @RequestMapping("/teamLevel_add")
    public String teamLevelAdd() {
        return PREFIX + "teamLevel_add.html";
    }

    /**
     * 跳转到修改团队返佣
     */
    @RequestMapping("/teamLevel_edit")
    public String teamLevelEdit(Long teamLevelId, Model model) {
        TeamLevel condition = this.teamLevelService.getById(teamLevelId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "teamLevel_edit.html";
    }

    /**
     * 团队返佣详情
     */
    @RequestMapping(value = "/detail/{teamLevelId}")
    @ResponseBody
    public Object detail(@PathVariable("teamLevelId") Long teamLevelId) {
        TeamLevel entity = teamLevelService.getById(teamLevelId);
      //  TeamLevelDto conditionDto = new TeamLevelDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询团队返佣列表
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
        Page<Map<String, Object>> result = teamLevelService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new TeamLevelWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑团队返佣
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "teamLevelId", dict = TeamLevelMap.class)
    @ResponseBody
    public ResponseData edit(TeamLevel teamLevel) {
        teamLevelService.updateById(teamLevel);
        return SUCCESS_TIP;
    }

    /**
     * 添加团队返佣
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加团队返佣", key = "name", dict = TeamLevelMap.class)
    @ResponseBody
    public ResponseData add(TeamLevel teamLevel, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (teamLevel == null) {
            return ResponseData.error("参数不能为空");
        }
        Integer level = teamLevel.getLevel();
        if(level<=0&& new Integer(teamLevel.getNumber())<=0){
            return ResponseData.error("等级以及数量需要大于零");
        }
        if(level>1){
            TeamLevel teamLevel1 = new TeamLevel();
            teamLevel1.setLevel(level-1);
            TeamLevel one = teamLevelService.getOne(new QueryWrapper<>(teamLevel1));
            if(one==null){
                return ResponseData.error("未设置"+(level-1)+"级前不可设置"+level+"级");

            }
        }

        this.teamLevelService.addTeamLevel(teamLevel);
        return SUCCESS_TIP;
    }

    /**
     * 删除团队返佣
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除团队返佣", key = "teamLevelId", dict = TeamLevelMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long teamLevelId) {
        if (ToolUtil.isEmpty(teamLevelId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.teamLevelService.deleteTeamLevel(teamLevelId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{teamLevelId}")
    @ResponseBody
    public Object content(@PathVariable("teamLevelId") Long id) {
        TeamLevel teamLevel = teamLevelService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(teamLevel);
        return super.warpObject(new TeamLevelWrapper(stringObjectMap));
    }
}
