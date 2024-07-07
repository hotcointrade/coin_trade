package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.ProConst;
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
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.app.service.VerifyService;
import cn.stylefeng.guns.modular.app.wrapper.VerifyWrapper;
import cn.stylefeng.guns.modular.app.entity.Verify;
import cn.stylefeng.guns.modular.app.constant.VerifyMap;

/**
 * 实名认证控制器
 *
 * @author yaying.liu
 * @Date 2020-03-11 16:31:57
 */
@Controller
@RequestMapping("/verify")
public class VerifyController extends BaseController {

    private String PREFIX = "/modular/app/verify/";

     @Autowired
     private VerifyService verifyService;

     @Autowired
     private MemberService memberService;
    /**
     * 跳转到实名认证首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "verify.html";
    }

    /**
     * 跳转到添加实名认证
     */
    @RequestMapping("/verify_add")
    public String verifyAdd() {
        return PREFIX + "verify_add.html";
    }

    /**
     * 跳转到修改实名认证
     */
    @RequestMapping("/verify_edit")
    public String verifyEdit(Long verifyId, Model model) {
        Verify condition = this.verifyService.getById(verifyId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "verify_edit.html";
    }

    /**
     * 实名认证详情
     */
    @RequestMapping(value = "/detail/{verifyId}")
    @ResponseBody
    public Object detail(@PathVariable("verifyId") Long verifyId) {
        Verify entity = verifyService.getById(verifyId);
      //  VerifyDto conditionDto = new VerifyDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询实名认证列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = verifyService.selectByCondition(condition);
        Page wrapped = new VerifyWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑实名认证
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "verifyId", dict = VerifyMap.class)
    @ResponseBody
    public ResponseData edit(Verify verify) {
        verifyService.updateById(verify);
        return SUCCESS_TIP;
    }

    /**
     * 添加实名认证
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加实名认证", key = "name", dict = VerifyMap.class)
    @ResponseBody
    public ResponseData add(Verify verify, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (verify == null) {
            return ResponseData.error("参数不能为空");
        }
        this.verifyService.addVerify(verify);
        return SUCCESS_TIP;
    }

    /**
     * 删除实名认证
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除实名认证", key = "verifyId", dict = VerifyMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long verifyId) {
        if (ToolUtil.isEmpty(verifyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.verifyService.deleteVerify(verifyId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{verifyId}")
    @ResponseBody
    public Object content(@PathVariable("verifyId") Long id) {
        Verify verify = verifyService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(verify);
        return super.warpObject(new VerifyWrapper(stringObjectMap));
    }

    /**
     * 审核通过
     * @param id
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    public  Object pass(Long id){
        Verify entity = verifyService.getById(id);
        //审核中
        if(entity.getStatus().equals(ProConst.Status.CHECK.code)){
            entity.setStatus(ProConst.Status.PASS.code);
            verifyService.updateById(entity);
            Member member= F.me().getMember(entity.getMemberId());
            member.setRealStatus("1");
            member.setRealName(entity.getFistName()+entity.getLastName());
            member.setNickName(entity.getFistName()+entity.getLastName());
            memberService.updateById(member);
        }else{
            return null;
        }
        return SUCCESS_TIP;
    }

    /**
     * 审核失败
     * @param id
     * @return
     */
    @RequestMapping("/fail")
    @ResponseBody
    public  Object fail(Long id)
    {
        Verify entity = verifyService.getById(id);

        //审核中
        if(entity.getStatus().equals(ProConst.Status.CHECK.code))
        {
            entity.setStatus(ProConst.Status.REJECT.code);

            verifyService.updateById(entity);

            Member member= F.me().getMember(entity.getMemberId());
            member.setRealStatus("0");
            memberService.updateById(member);
        }
        else
        {
            return null;
        }
        return SUCCESS_TIP;
    }
    
}