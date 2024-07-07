package cn.stylefeng.guns.modular.mining.controller;

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
import cn.stylefeng.guns.modular.mining.service.MiningOrderDetailService;
import cn.stylefeng.guns.modular.mining.wrapper.MiningOrderDetailWrapper;
import cn.stylefeng.guns.modular.mining.entity.MiningOrderDetail;
import cn.stylefeng.guns.modular.mining.constant.MiningOrderDetailMap;

/**
 * 矿机收益控制器
 *
 * @author yaying.liu
 * @since 2022-06-07 13:36:19
 */
@Controller
@RequestMapping("/miningOrderDetail")
public class MiningOrderDetailController extends BaseController {

    private String PREFIX = "/modular/mining/miningOrderDetail/";

     @Autowired
     private MiningOrderDetailService miningOrderDetailService;

    /**
     * 跳转到矿机收益首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "miningOrderDetail.html";
    }

    /**
     * 跳转到添加矿机收益
     */
    @RequestMapping("/miningOrderDetail_add")
    public String miningOrderDetailAdd() {
        return PREFIX + "miningOrderDetail_add.html";
    }

    /**
     * 跳转到修改矿机收益
     */
    @RequestMapping("/miningOrderDetail_edit")
    public String miningOrderDetailEdit(Long miningOrderDetailId, Model model) {
        MiningOrderDetail condition = this.miningOrderDetailService.getById(miningOrderDetailId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "miningOrderDetail_edit.html";
    }

    /**
     * 矿机收益详情
     */
    @RequestMapping(value = "/detail/{miningOrderDetailId}")
    @ResponseBody
    public Object detail(@PathVariable("miningOrderDetailId") Long miningOrderDetailId) {
        MiningOrderDetail entity = miningOrderDetailService.getById(miningOrderDetailId);
      //  MiningOrderDetailDto conditionDto = new MiningOrderDetailDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询矿机收益列表
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
        Page<Map<String, Object>> result = miningOrderDetailService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new MiningOrderDetailWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑矿机收益
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "miningOrderDetailId", dict = MiningOrderDetailMap.class)
    @ResponseBody
    public ResponseData edit(MiningOrderDetail miningOrderDetail) {
        miningOrderDetailService.updateById(miningOrderDetail);
        return SUCCESS_TIP;
    }

    /**
     * 添加矿机收益
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加矿机收益", key = "name", dict = MiningOrderDetailMap.class)
    @ResponseBody
    public ResponseData add(MiningOrderDetail miningOrderDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (miningOrderDetail == null) {
            return ResponseData.error("参数不能为空");
        }
        this.miningOrderDetailService.addMiningOrderDetail(miningOrderDetail);
        return SUCCESS_TIP;
    }

    /**
     * 删除矿机收益
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除矿机收益", key = "miningOrderDetailId", dict = MiningOrderDetailMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long miningOrderDetailId) {
        if (ToolUtil.isEmpty(miningOrderDetailId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.miningOrderDetailService.deleteMiningOrderDetail(miningOrderDetailId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{miningOrderDetailId}")
    @ResponseBody
    public Object content(@PathVariable("miningOrderDetailId") Long id) {
        MiningOrderDetail miningOrderDetail = miningOrderDetailService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(miningOrderDetail);
        return super.warpObject(new MiningOrderDetailWrapper(stringObjectMap));
    }
}