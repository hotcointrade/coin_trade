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
import cn.stylefeng.guns.modular.app.service.CoinApplyService;
import cn.stylefeng.guns.modular.app.wrapper.CoinApplyWrapper;
import cn.stylefeng.guns.modular.app.entity.CoinApply;
import cn.stylefeng.guns.modular.app.constant.CoinApplyMap;

/**
 * 上币申请控制器
 *
 * @author yaying.liu
 * @since 2022-02-20 20:37:01
 */
@Controller
@RequestMapping("/coinApply")
public class CoinApplyController extends BaseController {

    private String PREFIX = "/modular/app/coinApply/";

     @Autowired
     private CoinApplyService coinApplyService;

    /**
     * 跳转到上币申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "coinApply.html";
    }

    /**
     * 跳转到添加上币申请
     */
    @RequestMapping("/coinApply_add")
    public String coinApplyAdd() {
        return PREFIX + "coinApply_add.html";
    }

    /**
     * 跳转到修改上币申请
     */
    @RequestMapping("/coinApply_edit")
    public String coinApplyEdit(Long coinApplyId, Model model) {
        CoinApply condition = this.coinApplyService.getById(coinApplyId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "coinApply_edit.html";
    }

    /**
     * 上币申请详情
     */
    @RequestMapping(value = "/detail/{coinApplyId}")
    @ResponseBody
    public Object detail(@PathVariable("coinApplyId") Long coinApplyId) {
        CoinApply entity = coinApplyService.getById(coinApplyId);
      //  CoinApplyDto conditionDto = new CoinApplyDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询上币申请列表
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
        Page<Map<String, Object>> result = coinApplyService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new CoinApplyWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑上币申请
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "coinApplyId", dict = CoinApplyMap.class)
    @ResponseBody
    public ResponseData edit(CoinApply coinApply) {
        coinApplyService.updateById(coinApply);
        return SUCCESS_TIP;
    }

    /**
     * 添加上币申请
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加上币申请", key = "name", dict = CoinApplyMap.class)
    @ResponseBody
    public ResponseData add(CoinApply coinApply, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (coinApply == null) {
            return ResponseData.error("参数不能为空");
        }
        this.coinApplyService.addCoinApply(coinApply);
        return SUCCESS_TIP;
    }

    /**
     * 删除上币申请
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除上币申请", key = "coinApplyId", dict = CoinApplyMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long coinApplyId) {
        if (ToolUtil.isEmpty(coinApplyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.coinApplyService.deleteCoinApply(coinApplyId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{coinApplyId}")
    @ResponseBody
    public Object content(@PathVariable("coinApplyId") Long id) {
        CoinApply coinApply = coinApplyService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(coinApply);
        return super.warpObject(new CoinApplyWrapper(stringObjectMap));
    }
}