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
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.guns.modular.fin.wrapper.LegalWrapper;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.constant.LegalMap;

/**
 * 法币账户控制器
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:11:25
 */
@Controller
@RequestMapping("/legal")
public class LegalController extends BaseController {

    private String PREFIX = "/modular/fin/legal/";

     @Autowired
     private LegalService legalService;

    /**
     * 跳转到法币账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "legal.html";
    }

    /**
     * 跳转到添加法币账户
     */
    @RequestMapping("/legal_add")
    public String legalAdd() {
        return PREFIX + "legal_add.html";
    }

    /**
     * 跳转到修改法币账户
     */
    @RequestMapping("/legal_edit")
    public String legalEdit(Long legalId, Model model) {
        Legal condition = this.legalService.getById(legalId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "legal_edit.html";
    }

    /**
     * 法币账户详情
     */
    @RequestMapping(value = "/detail/{legalId}")
    @ResponseBody
    public Object detail(@PathVariable("legalId") Long legalId) {
        Legal entity = legalService.getById(legalId);
      //  LegalDto conditionDto = new LegalDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询法币账户列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition,@RequestParam(required = false)Double minPrice,@RequestParam(required = false)Double maxPrice) {
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        //根据条件查询
        Page<Map<String, Object>> result = legalService.selectByCondition(minPrice,maxPrice,condition,memberId,recommendIds);
        Page wrapped = new LegalWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑法币账户
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(Legal legal) {
        legalService.edit(legal);
        return SUCCESS_TIP;
    }

    /**
     * 添加法币账户
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加法币账户", key = "name", dict = LegalMap.class)
    @ResponseBody
    public ResponseData add(Legal legal, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (legal == null) {
            return ResponseData.error("参数不能为空");
        }
        this.legalService.addLegal(legal);
        return SUCCESS_TIP;
    }

    /**
     * 删除法币账户
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除法币账户", key = "legalId", dict = LegalMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long legalId) {
        if (ToolUtil.isEmpty(legalId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.legalService.deleteLegal(legalId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{legalId}")
    @ResponseBody
    public Object content(@PathVariable("legalId") Long id) {
        Legal legal = legalService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(legal);
        return super.warpObject(new LegalWrapper(stringObjectMap));
    }
}
