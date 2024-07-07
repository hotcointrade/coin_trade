package cn.stylefeng.guns.modular.fin.wrapper;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 流水记录Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:14
 */
public class CashflowWrapper extends BaseControllerWrapper
{

    public CashflowWrapper(Map<String, Object> single)
    {
        super(single);
    }

    public CashflowWrapper(List<Map<String, Object>> multi)
    {
        super(multi);
    }

    public CashflowWrapper(Page<Map<String, Object>> page)
    {
        super(page);
    }

    public CashflowWrapper(PageResult<Map<String, Object>> pageResult)
    {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map)
    {
        Long memberId = (Long) map.get("memberId");
        Member member = F.me().getMember(memberId);

        map.put("memberIdValue", member!=null?member.getAccount():memberId);


        String walletType = (String) map.get("walletType");
        if (walletType != null)
        {
            for (ProConst.WalletBigType e : ProConst.WalletBigType.values())
            {
                if (e.code.equals(walletType))
                {
                    map.put("walletTypeValue", e.value);

                }
            }
        }

         String itemCode = (String) map.get("itemCode");
        if (itemCode != null)
        {
            for (ProConst.ItemCode e : ProConst.ItemCode.values())
            {
                if (e.code.equals(itemCode))
                {
                    map.put("itemCode", e.value);

                }
            }
        }




        Long flowOp = (Long) map.get("flowOp");
        if (flowOp != null)
        {
            for (ProConst.CashFlowOpEnum e : ProConst.CashFlowOpEnum.values())
            {
                if (e.code.equals(flowOp))
                {
                    map.put("flowOpValue", e.value);
                }
            }
        }
        String flowType = (String) map.get("flowType");
        if (flowType != null)
        {
            for (ProConst.CashFlowTypeEnum e : ProConst.CashFlowTypeEnum.values())
            {
                if (e.getCode().equals(flowType))
                {
                    map.put("flowTypeValue", e.getValue());
                }
            }
        }



        Long fromId = (Long) map.get("fromId");
        if (fromId.longValue() == -1)
        {
            map.put("fromIdValue", "平台");
        }
        else
        {
            Member member1 = F.me().getMember(fromId);
            map.put("fromIdValue", member1!=null?member1.getAccount():fromId);
        }
        Long toId = (Long) map.get("toId");
        if (toId.longValue() == -1)
        {
            map.put("toIdValue", "平台");
        }
        else
        {
            Member member1 = F.me().getMember(toId);
            map.put("toIdValue",member1!=null? member1.getAccount():toId);
        }


    }
}
