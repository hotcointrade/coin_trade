package cn.stylefeng.guns.modular.e.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.e.entity.MatchDetail;
import cn.stylefeng.guns.modular.e.service.MatchDetailService;
import cn.stylefeng.guns.modular.e.wrapper.MatchDetailWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 撮合交易成交明细控制器
 *
 * @author yaying.liu
 * @Date 2020-05-20 10:29:28
 */
@Controller
@RequestMapping("/matchDetail")
public class MatchDetailController extends BaseController {

    private String PREFIX = "/modular/e/matchDetail/";

     @Autowired
     private MatchDetailService matchDetailService;

    /**
     * 跳转到撮合交易成交明细首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "matchDetail.html";
    }

    /**
     * 跳转到添加撮合交易成交明细
     */
    @RequestMapping("/matchDetail_add")
    public String matchDetailAdd() {
        return PREFIX + "matchDetail_add.html";
    }

    /**
     * 跳转到修改撮合交易成交明细
     */
    @RequestMapping("/matchDetail_edit")
    public String matchDetailEdit(Long matchDetailId, Model model) {
        MatchDetail condition = this.matchDetailService.getById(matchDetailId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "matchDetail_edit.html";
    }

    /**
     * 撮合交易成交明细详情
     */
    @RequestMapping(value = "/detail/{matchDetailId}")
    @ResponseBody
    public Object detail(@PathVariable("matchDetailId") Long matchDetailId) {
        MatchDetail entity = matchDetailService.getById(matchDetailId);
      //  MatchDetailDto conditionDto = new MatchDetailDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询撮合交易成交明细列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = matchDetailService.selectByCondition(condition);
        Page wrapped = new MatchDetailWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑撮合交易成交明细
     */
    @RequestMapping("/edit")
   // @BussinessLog(value = "编辑参数", key = "matchDetailId", dict = MatchDetailMap.class)
    @ResponseBody
    public ResponseData edit(MatchDetail matchDetail) {
        matchDetailService.updateById(matchDetail);
        return SUCCESS_TIP;
    }

    /**
     * 添加撮合交易成交明细
     */
    @RequestMapping("/add")
   // @BussinessLog(value = "添加撮合交易成交明细", key = "name", dict = MatchDetailMap.class)
    @ResponseBody
    public ResponseData add(MatchDetail matchDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (matchDetail == null) {
            return ResponseData.error("参数不能为空");
        }
        this.matchDetailService.addMatchDetail(matchDetail);
        return SUCCESS_TIP;
    }

    /**
     * 删除撮合交易成交明细
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除撮合交易成交明细", key = "matchDetailId", dict = MatchDetailMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long matchDetailId) {
        if (ToolUtil.isEmpty(matchDetailId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.matchDetailService.deleteMatchDetail(matchDetailId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{matchDetailId}")
    @ResponseBody
    public Object content(@PathVariable("matchDetailId") Long id) {
        MatchDetail matchDetail = matchDetailService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(matchDetail);
        return super.warpObject(new MatchDetailWrapper(stringObjectMap));
    }
}