package cn.stylefeng.guns.modular.otc.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.OtcService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Map;

import cn.stylefeng.guns.modular.otc.entity.Deposit;
import cn.stylefeng.guns.modular.otc.mapper.DepositMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户押金Service
 *
 * @author yaying.liu
 * @since 2020-07-01 11:55:46
 */
@Service
public class DepositService extends ServiceImpl<DepositMapper, Deposit>
{

    @Autowired
    LegalService legalService;

    @Autowired
    OtcService otcService;

    /**
     * 查询用户押金
     */
    public Page<Map<String, Object>> selectByCondition(String condition,String coin,
                                                       String status,String remark, String beginTime,
                                                       String endTime,Long memberId,String recommendIds)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition,coin,status,remark, beginTime, endTime,memberId,recommendIds);
    }

    /**
     * 删除用户押金
     */
    public void deleteDeposit(Long depositId)
    {
        Deposit entity = this.baseMapper.selectById(depositId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加用户押金
     */
    public void addDeposit(Deposit deposit)
    {
        this.baseMapper.insert(deposit);
    }

    //修改用户押金
    @Transactional(rollbackFor = Exception.class)
    public ResponseData edit(Deposit newDeposit) throws Exception
    {
        //old data
        Deposit oldDeposit = getById(newDeposit.getDepositId());
        Member member = F.me().getMember(oldDeposit.getMemberId());
        if (member == null)
            throw new Exception();

        //存在退还审核，不可修改
        if(otcService.getCheckBack(member)!=null)
            return ResponseData.error("存在退还审核单，不可修改");

        Legal legal = F.me().getLegal(oldDeposit.getMemberId(), oldDeposit.getCoin());
        BigDecimal beforeLock = legal.getLockedPrice();
        //补充押金
        if (newDeposit.getNumber().compareTo(oldDeposit.getNumber()) > 0)
        {
            BigDecimal subPrice = newDeposit.getNumber().subtract(oldDeposit.getNumber());

            BigDecimal afterLock = beforeLock.add(subPrice);

            legal.setLockedPrice(afterLock).setUpdateUser(Constant.SYS_PLATFORM);
            if (!legalService.updateById(legal))
                throw new UpdateDataException(100);

            //冻结流水
            F.me().saveCashflow(oldDeposit.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.PLAT_ADD_MAKE,
                    subPrice, oldDeposit.getCoin(), subPrice, oldDeposit.getCoin(), BigDecimal.ZERO, oldDeposit.getCoin(),
                    ProConst.ItemCode.LOCKED, oldDeposit.getCoin(), null, null,
                    beforeLock, afterLock, Constant.SYS_PLATFORM, oldDeposit.getMemberId());

        }
        //减少押金
        if (newDeposit.getNumber().compareTo(oldDeposit.getNumber()) < 0)
        {
            BigDecimal subPrice = oldDeposit.getNumber().subtract(newDeposit.getNumber());
            BigDecimal afterLock = beforeLock.subtract(subPrice);

            legal.setLockedPrice(afterLock).setUpdateUser(Constant.SYS_PLATFORM);

            if (!legalService.updateById(legal))
                throw new UpdateDataException(100);

            //冻结流水
            F.me().saveCashflow(oldDeposit.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.PLAT_SUB_MAKE,
                    subPrice, oldDeposit.getCoin(), subPrice, oldDeposit.getCoin(), BigDecimal.ZERO, oldDeposit.getCoin(),
                    ProConst.ItemCode.LOCKED, oldDeposit.getCoin(), null, null,
                    beforeLock, afterLock, oldDeposit.getMemberId(), Constant.SYS_PLATFORM);

        }
        //全额缴纳-1   待补缴-0
        newDeposit.setStatus(new BigDecimal(member.getDeposit()).compareTo(newDeposit.getNumber()) > 0 ? "0" : "1");

        updateById(newDeposit);
        return ResponseData.success();
    }
}