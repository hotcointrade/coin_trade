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
import cn.stylefeng.guns.modular.app.service.CommunityService;
import cn.stylefeng.guns.modular.app.wrapper.CommunityWrapper;
import cn.stylefeng.guns.modular.app.entity.Community;
import cn.stylefeng.guns.modular.app.constant.CommunityMap;

/**
 * 官方社区控制器
 *
 * @author yaying.liu
 * @since 2022-10-18 18:24:14
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    private String PREFIX = "/modular/app/community/";

     @Autowired
     private CommunityService communityService;

    /**
     * 跳转到官方社区首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "community.html";
    }

    /**
     * 跳转到添加官方社区
     */
    @RequestMapping("/community_add")
    public String communityAdd() {
        return PREFIX + "community_add.html";
    }

    /**
     * 跳转到修改官方社区
     */
    @RequestMapping("/community_edit")
    public String communityEdit(Long communityId, Model model) {
        Community condition = this.communityService.getById(communityId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "community_edit.html";
    }

    /**
     * 官方社区详情
     */
    @RequestMapping(value = "/detail/{communityId}")
    @ResponseBody
    public Object detail(@PathVariable("communityId") Long communityId) {
        Community entity = communityService.getById(communityId);
      //  CommunityDto conditionDto = new CommunityDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询官方社区列表
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
        Page<Map<String, Object>> result = communityService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new CommunityWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑官方社区
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "communityId", dict = CommunityMap.class)
    @ResponseBody
    public ResponseData edit(Community community) {
        communityService.updateById(community);
        return SUCCESS_TIP;
    }

    /**
     * 添加官方社区
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加官方社区", key = "name", dict = CommunityMap.class)
    @ResponseBody
    public ResponseData add(Community community, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (community == null) {
            return ResponseData.error("参数不能为空");
        }
        this.communityService.addCommunity(community);
        return SUCCESS_TIP;
    }

    /**
     * 删除官方社区
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除官方社区", key = "communityId", dict = CommunityMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long communityId) {
        if (ToolUtil.isEmpty(communityId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.communityService.deleteCommunity(communityId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{communityId}")
    @ResponseBody
    public Object content(@PathVariable("communityId") Long id) {
        Community community = communityService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(community);
        return super.warpObject(new CommunityWrapper(stringObjectMap));
    }
}