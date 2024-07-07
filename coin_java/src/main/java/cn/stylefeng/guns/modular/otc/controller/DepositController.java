package cn.stylefeng.guns.modular.otc.controller;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.service.LegalService;
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

import java.math.BigDecimal;
import java.util.Map;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.otc.service.DepositService;
import cn.stylefeng.guns.modular.otc.wrapper.DepositWrapper;
import cn.stylefeng.guns.modular.otc.entity.Deposit;
import cn.stylefeng.guns.modular.otc.constant.DepositMap;

import javax.annotation.Resource;

/**
 * 用户押金控制器
 *
 * @author yaying.liu
 * @since 2020-07-01 11:55:45
 */
@Controller
@RequestMapping("/deposit")
public class DepositController extends BaseController {

    private String PREFIX = "/modular/otc/deposit/";

    @Autowired
    private DepositService depositService;

    @Resource
    private MemberService memberService;

    @Resource
    private LegalService legalService;

    /**
     * 跳转到用户押金首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "deposit.html";
    }

    /**
     * 跳转到添加用户押金
     */
    @RequestMapping("/deposit_add")
    public String depositAdd() {
        return PREFIX + "deposit_add.html";
    }

    /**
     * 跳转到修改用户押金
     */
    @RequestMapping("/deposit_edit")
    public String depositEdit(Long depositId, Model model) {
        Deposit condition = this.depositService.getById(depositId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "deposit_edit.html";
    }

    /**
     * 用户押金详情
     */
    @RequestMapping(value = "/detail/{depositId}")
    @ResponseBody
    public Object detail(@PathVariable("depositId") Long depositId) {
        Deposit entity = depositService.getById(depositId);
        //  DepositDto conditionDto = new DepositDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询用户押金列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition

            , @RequestParam(required = false) String coin
            , @RequestParam(required = false) String status
            , @RequestParam(required = false) String timeLimit
            , @RequestParam(required = false) String remark

    ) {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = depositService.selectByCondition(condition, coin,
                status,remark,
                beginTime, endTime,memberId,recommendIds);
        Page wrapped = new DepositWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }


    /**
     * 编辑用户押金
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "depositId", dict = DepositMap.class)
    @ResponseBody
    public ResponseData edit(Deposit deposit) throws Exception {
        return depositService.edit(deposit);
    }

    /**
     * 统计
     */
    @RequestMapping("/total")
    @ResponseBody
    public Object total() {
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        return depositService.getBaseMapper().total(memberId,recommendIds);
    }

    /**
     * 添加用户押金
     */
    @RequestMapping("/add")
    //  @BussinessLog(value = "添加用户押金", key = "name", dict = DepositMap.class)
    @ResponseBody
    public ResponseData add(Deposit deposit, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (deposit == null) {
            return ResponseData.error("参数不能为空");
        }
        this.depositService.addDeposit(deposit);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户押金
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除用户押金", key = "depositId", dict = DepositMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long depositId) {
        if (ToolUtil.isEmpty(depositId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.depositService.deleteDeposit(depositId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{depositId}")
    @ResponseBody
    public Object content(@PathVariable("depositId") Long id) {
        Deposit deposit = depositService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(deposit);
        return super.warpObject(new DepositWrapper(stringObjectMap));
    }

    /**
     * 审核通过
     *
     * @param id
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    public Object pass(Long id) {
        Deposit entity = depositService.getById(id);

        //审核中
        if (entity.getRemark().equals(ProConst.Status.CHECK.code())) {
            entity.setRemark(ProConst.Status.PASS.code());
            depositService.updateById(entity);
            Member member = F.me().getMember(entity.getMemberId());
            member.setOtc("1").setHasOtc("1").setUpdateUser(Constant.SYS_PLATFORM);
            memberService.updateById(member);

        } else {
            return null;
        }
        return SUCCESS_TIP;
    }

    /**
     * 审核失败
     *
     * @param id
     * @return
     */
    @RequestMapping("/fail")
    @ResponseBody
    public Object fail(Long id) {
        Deposit entity = depositService.getById(id);

        //审核中
        if (entity.getRemark().equals(ProConst.Status.CHECK.code())) {

//            entity.setRemark(ProConst.Status.REJECT.code()).setDel("N");

            Member member = F.me().getMember(entity.getMemberId());
            member.setOtc("3").setUpdateUser(Constant.SYS_PLATFORM);
            memberService.updateById(member);

            Legal legal=F.me().getLegal(member.getMemberId(),entity.getCoin());
            BigDecimal price=entity.getNumber();
            String type=entity.getCoin();
            BigDecimal beforeUsed = legal.getUsedPrice();
            BigDecimal afterUsed = legal.getUsedPrice().add(price);

            BigDecimal beforeLock = legal.getLockedPrice();
            BigDecimal afterLock = legal.getLockedPrice().subtract(price);

            legal.setUsedPrice(afterUsed)
                    .setLockedPrice(afterLock)
                    .setUpdateUser(Constant.SYS_PLATFORM);
            if (!legalService.updateById(legal))
            {
                throw new UpdateDataException(100);
            }
            //可用流水 +
            F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.DEPOSIT_FAIL,
                    price, type, price, type, BigDecimal.ZERO, type,
                    ProConst.ItemCode.USED, type, null, null,
                    beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
            //冻结流水 -
            F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.DEPOSIT_FAIL,
                    price, type, price, type, BigDecimal.ZERO, type,
                    ProConst.ItemCode.LOCKED, type, null, null,
                    beforeLock, afterLock, member.getMemberId(), member.getMemberId());

            //删除
            depositService.removeById(entity);

        } else {
            return null;
        }
        return SUCCESS_TIP;
    }


}