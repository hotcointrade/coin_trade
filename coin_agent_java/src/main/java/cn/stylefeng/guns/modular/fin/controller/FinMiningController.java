package cn.stylefeng.guns.modular.fin.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
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
import cn.stylefeng.guns.modular.fin.service.FinMiningService;
import cn.stylefeng.guns.modular.fin.wrapper.FinMiningWrapper;
import cn.stylefeng.guns.modular.fin.entity.FinMining;
import cn.stylefeng.guns.modular.fin.constant.FinMiningMap;

/**
 * 矿机账户控制器
 *
 * @author yaying.liu
 * @since 2022-06-07 13:37:29
 */
@Controller
@RequestMapping("/finMining")
public class FinMiningController extends BaseController {

    private String PREFIX = "/modular/fin/finMining/";

     @Autowired
     private FinMiningService finMiningService;

    /**
     * 跳转到矿机账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "finMining.html";
    }

    /**
     * 跳转到添加矿机账户
     */
    @RequestMapping("/finMining_add")
    public String finMiningAdd() {
        return PREFIX + "finMining_add.html";
    }

    /**
     * 跳转到修改矿机账户
     */
    @RequestMapping("/finMining_edit")
    public String finMiningEdit(Long finMiningId, Model model) {
        FinMining condition = this.finMiningService.getById(finMiningId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "finMining_edit.html";
    }

    /**
     * 矿机账户详情
     */
    @RequestMapping(value = "/detail/{finMiningId}")
    @ResponseBody
    public Object detail(@PathVariable("finMiningId") Long finMiningId) {
        FinMining entity = finMiningService.getById(finMiningId);
      //  FinMiningDto conditionDto = new FinMiningDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询矿机账户列表
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
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = finMiningService.selectByCondition(condition,beginTime,endTime,memberId,recommendIds);
        Page wrapped = new FinMiningWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑矿机账户
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "finMiningId", dict = FinMiningMap.class)
    @ResponseBody
    public ResponseData edit(FinMining finMining) {
        return finMiningService.edit(finMining);

    }

    /**
     * 添加矿机账户
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加矿机账户", key = "name", dict = FinMiningMap.class)
    @ResponseBody
    public ResponseData add(FinMining finMining, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (finMining == null) {
            return ResponseData.error("参数不能为空");
        }
        this.finMiningService.addFinMining(finMining);
        return SUCCESS_TIP;
    }

    /**
     * 删除矿机账户
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除矿机账户", key = "finMiningId", dict = FinMiningMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long finMiningId) {
        if (ToolUtil.isEmpty(finMiningId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.finMiningService.deleteFinMining(finMiningId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{finMiningId}")
    @ResponseBody
    public Object content(@PathVariable("finMiningId") Long id) {
        FinMining finMining = finMiningService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(finMining);
        return super.warpObject(new FinMiningWrapper(stringObjectMap));
    }
}
