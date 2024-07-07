package cn.stylefeng.guns.modular.e.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.e.constant.CompactMap;
import cn.stylefeng.guns.modular.e.entity.Compact;
import cn.stylefeng.guns.modular.e.entity.FuturesCompact;
import cn.stylefeng.guns.modular.e.service.CompactService;
import cn.stylefeng.guns.modular.e.service.FuturesCompactService;
import cn.stylefeng.guns.modular.e.wrapper.CompactWrapper;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 合约订单控制器
 *
 * @author yaying.liu
 * @Date 2020-03-23 14:35:23
 */
@Controller
@RequestMapping("/futuresCompact")
public class FuturesCompactController extends BaseController {

    private String PREFIX = "/modular/e/futures/";

     @Autowired
     private FuturesCompactService compactService;

    /**
     * 跳转到合约订单首页
     */
    @RequestMapping("")
    public String index(Model model) {
        Map<String, Object> map = compactService.getTotalMap(null);
        model.addAllAttributes(map);
        return PREFIX + "compact.html";
    }

    /**
     * 跳转到添加合约订单
     */
    @RequestMapping("/compact_add")
    public String compactAdd() {
        return PREFIX + "compact_add.html";
    }

    /**
     * 跳转到修改合约订单
     */
    @RequestMapping("/compact_edit")
    public String compactEdit(Long compactId, Model model) {
        FuturesCompact condition = this.compactService.getById(compactId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "compact_edit.html";
    }

    /**
     * 合约订单详情
     */
    @RequestMapping(value = "/detail/{compactId}")
    @ResponseBody
    public Object detail(@PathVariable("compactId") Long compactId) {
       return compactService.getById(compactId);
      //  CompactDto conditionDto = new CompactDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        //return entity;
    }


    /**
     * 查询合约订单列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition,
                       @RequestParam(required = false) String phone,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String dealWay,
                       @RequestParam(required = false) String compactType,
                       @RequestParam(required = false) String symbols,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String exitType,
                       @RequestParam(required = false) String exitTime) {
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }

        Map<String,Object> map = new HashMap<>();
        map.put("condition",condition);
        map.put("memberId",memberId);
        map.put("recommendIds",recommendIds);
        map.put("email",email);
        map.put("phone",phone);
        map.put("dealWay",dealWay);
        map.put("compactType",compactType);
        map.put("symbols",symbols);
        map.put("status",status);
        map.put("exitType",exitType);
        map.put("status",status);
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(exitTime)) {
            String[] split = exitTime.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        map.put("exitBeginTime",beginTime);
        map.put("exitEndTime",endTime);
        Page<Map<String, Object>> result = compactService.selectByCondition(map);
        Page wrapped = new CompactWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    @RequestMapping("/searchStatistics")
    @ResponseBody
    public ResponseData searchStatistics(@RequestParam(required = false) String condition,
                                         @RequestParam(required = false) String phone,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String dealWay,
                                         @RequestParam(required = false) String compactType,
                                         @RequestParam(required = false) String symbols,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String exitType,
                                         @RequestParam(required = false) String exitTime){

        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }

        Map<String,Object> map = new HashMap<>();
        map.put("condition",condition);
        map.put("memberId",memberId);
        map.put("recommendIds",recommendIds);
        map.put("email",email);
        map.put("phone",phone);
        map.put("dealWay",dealWay);
        map.put("compactType",compactType);
        map.put("symbols",symbols);
        map.put("status",status);
        map.put("exitType",exitType);
        map.put("status",status);
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(exitTime)) {
            String[] split = exitTime.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        map.put("exitBeginTime",beginTime);
        map.put("exitEndTime",endTime);
        Map<String ,Object> returnmap = compactService.getTotalMap(map);
        return ResponseData.success(returnmap);
    }


    /**
     * 编辑合约订单
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "compactId", dict = CompactMap.class)
    @ResponseBody
    public ResponseData edit(FuturesCompact compact) {
        compactService.updateById(compact);
        return SUCCESS_TIP;
    }

    /**
     * 添加合约订单
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加合约订单", key = "name", dict = CompactMap.class)
    @ResponseBody
    public ResponseData add(FuturesCompact compact, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (compact == null) {
            return ResponseData.error("参数不能为空");
        }
        this.compactService.addCompact(compact);
        return SUCCESS_TIP;
    }

    /**
     * 删除合约订单
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除合约订单", key = "compactId", dict = CompactMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long compactId) {
        if (ToolUtil.isEmpty(compactId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.compactService.deleteCompact(compactId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{compactId}")
    @ResponseBody
    public Object content(@PathVariable("compactId") Long id) {
        FuturesCompact compact = compactService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(compact);
        return super.warpObject(new CompactWrapper(stringObjectMap));
    }


}