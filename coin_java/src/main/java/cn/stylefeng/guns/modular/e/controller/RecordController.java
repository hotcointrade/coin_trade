package cn.stylefeng.guns.modular.e.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.e.constant.MatchMap;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.entity.Record;
import cn.stylefeng.guns.modular.e.service.MatchService;
import cn.stylefeng.guns.modular.e.service.RecordService;
import cn.stylefeng.guns.modular.e.wrapper.MatchWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 委托单信息控制器
 *
 * @author yaying.liu
 * @Date 2020-03-18 10:38:28
 */
@Controller
@RequestMapping("/record")
public class RecordController extends BaseController {

    private String PREFIX = "/modular/e/record/";

     @Autowired
     //private MatchService matchService;
     private RecordService recordService;

    /**
     * 跳转到委托单信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "record.html";
    }

    /**
     * 跳转到添加委托单信息
     */
    @RequestMapping("/match_add")
    public String matchAdd() {
        return PREFIX + "record_add.html";
    }

    /**
     * 跳转到修改委托单信息
     */
    @RequestMapping("/match_edit")
    public String matchEdit(Long matchId, Model model) {
        Record condition = this.recordService.getById(matchId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "record_edit.html";
    }

    /**
     * 委托单信息详情
     */
    @RequestMapping(value = "/detail/{matchId}")
    @ResponseBody
    public Object detail(@PathVariable("matchId") Long matchId) {
        Record entity = recordService.getById(matchId);
      //  MatchDto conditionDto = new MatchDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询委托单信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(
            @RequestParam(required = false) String condition
            ,@RequestParam(required = false) String  timeLimit
            ,@RequestParam(required = false) String  status

                       ) {

        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Map<String,Object> map=new HashedMap();
        map.put("condition",condition);
        map.put("status",status);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        map.put("memberId",memberId);
        map.put("recommendIds",recommendIds);

        //根据条件查询
        Page<Map<String, Object>> result = recordService.selectByCondition(map);
        Page wrapped = new MatchWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑委托单信息
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "matchId", dict = MatchMap.class)
    @ResponseBody
    public ResponseData edit(Record match) {
        recordService.updateById(match);
        return SUCCESS_TIP;
    }

    /**
     * 添加委托单信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加委托单信息", key = "name", dict = MatchMap.class)
    @ResponseBody
    public ResponseData add(Record match, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (match == null) {
            return ResponseData.error("参数不能为空");
        }
        this.recordService.addMatch(match);
        return SUCCESS_TIP;
    }

    /**
     * 删除委托单信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除委托单信息", key = "matchId", dict = MatchMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long matchId) {
        if (ToolUtil.isEmpty(matchId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.recordService.deleteMatch(matchId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{matchId}")
    @ResponseBody
    public Object content(@PathVariable("matchId") Long id) {
        Record match = recordService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(match);
        return super.warpObject(new MatchWrapper(stringObjectMap));
    }
}