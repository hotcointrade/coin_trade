package cn.stylefeng.guns.modular.otc.controller;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.OtcService;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.guns.modular.otc.entity.Deposit;
import cn.stylefeng.guns.modular.otc.service.DepositService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Map;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.otc.service.BackService;
import cn.stylefeng.guns.modular.otc.wrapper.BackWrapper;
import cn.stylefeng.guns.modular.otc.entity.Back;

/**
 * 退还押金控制器
 *
 * @author yaying.liu
 * @since 2020-07-09 14:33:04
 */
@Controller
@RequestMapping("/back")
@Slf4j
public class BackController extends BaseController
{

    private String PREFIX = "/modular/otc/back/";

    @Autowired
    private BackService backService;
    @Autowired
    private LegalService legalService;

    @Autowired
    private DepositService depositService;
    @Autowired
    private OtcService otcService;

    /**
     * 跳转到退还押金首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "back.html";
    }

    /**
     * 跳转到添加退还押金
     */
    @RequestMapping("/back_add")
    public String backAdd()
    {
        return PREFIX + "back_add.html";
    }

    /**
     * 跳转到修改退还押金
     */
    @RequestMapping("/back_edit")
    public String backEdit(Long backId, Model model)
    {
        Back condition = this.backService.getById(backId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "back_edit.html";
    }

    /**
     * 退还押金详情
     */
    @RequestMapping(value = "/detail/{backId}")
    @ResponseBody
    public Object detail(@PathVariable("backId") Long backId)
    {
        Back entity = backService.getById(backId);
        //  BackDto conditionDto = new BackDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }

    /**
     * 审核通过
     *
     * @param id
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object pass(Long id) throws Exception
    {
        Back entity = backService.getById(id);

        //审核中
        if (entity.getStatus().equals(ProConst.Status.CHECK.code()))
        {
            entity.setStatus(ProConst.Status.PASS.code());
            backService.updateById(entity);

            Legal legal = F.me().getLegal(entity.getMemberId(), entity.getCoin());

            if (legal.getLockedPrice().compareTo(entity.getNumber()) < 0)
                throw new Exception("法币冻结不足退还");

            BigDecimal beforeUsed = legal.getUsedPrice();
            BigDecimal afterUsed = legal.getUsedPrice().add(entity.getNumber());

            BigDecimal beforeLock = legal.getLockedPrice();
            BigDecimal afterLock = legal.getLockedPrice().subtract(entity.getNumber());

            legal.setUsedPrice(afterUsed)
                    .setLockedPrice(afterLock)
                    .setUpdateUser(cn.stylefeng.guns.modular.base.state.Constant.SYS_PLATFORM);
            if (!legalService.updateById(legal))
                throw new UpdateDataException(100);
            //流水 -可用
            F.me().saveCashflow(legal.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.BACK
                    , entity.getNumber(), entity.getCoin(), entity.getNumber(), entity.getCoin(), BigDecimal.ZERO, entity.getCoin(),
                    ProConst.ItemCode.USED, entity.getCoin(), null, null,
                    beforeUsed, afterUsed, legal.getMemberId(), legal.getMemberId());

            //流水-冻结
            F.me().saveCashflow(legal.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.BACK
                    , entity.getNumber(), entity.getCoin(), entity.getNumber(), entity.getCoin(), BigDecimal.ZERO, entity.getCoin(),
                    ProConst.ItemCode.LOCKED, entity.getCoin(), null, null,
                    beforeLock, afterLock, legal.getMemberId(), legal.getMemberId());

            Member member = F.me().getMember(legal.getMemberId());
            Deposit deposit = otcService.getDeposit(member);
            if(deposit==null)
                return null;
            //删除
            log.info(" >>>>> 押金退还：记录清除:{}",deposit.toString());
            depositService.removeById(deposit.getDepositId());
        }
        else
        {
            return null;
        }
        return SUCCESS_TIP;
    }

    /**
     * 查询退还押金列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String timeLimit)
    {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit))
        {
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
        Page<Map<String, Object>> result = backService.selectByCondition(condition, beginTime, endTime,memberId,recommendIds);
        Page wrapped = new BackWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑退还押金
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "backId", dict = BackMap.class)
    @ResponseBody
    public ResponseData edit(Back back)
    {
        backService.updateById(back);
        return SUCCESS_TIP;
    }

    /**
     * 添加退还押金
     */
    @RequestMapping("/add")
    //  @BussinessLog(value = "添加退还押金", key = "name", dict = BackMap.class)
    @ResponseBody
    public ResponseData add(Back back, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (back == null)
        {
            return ResponseData.error("参数不能为空");
        }
        this.backService.addBack(back);
        return SUCCESS_TIP;
    }

    /**
     * 删除退还押金
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除退还押金", key = "backId", dict = BackMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long backId)
    {
        if (ToolUtil.isEmpty(backId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.backService.deleteBack(backId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{backId}")
    @ResponseBody
    public Object content(@PathVariable("backId") Long id)
    {
        Back back = backService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(back);
        return super.warpObject(new BackWrapper(stringObjectMap));
    }
}