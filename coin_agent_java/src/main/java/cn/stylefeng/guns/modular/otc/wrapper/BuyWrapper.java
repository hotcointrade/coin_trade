package cn.stylefeng.guns.modular.otc.wrapper;

import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 购买挂单订单Wrapper
 *
 * @author yaying.liu
 * @since 2020-07-13 14:52:41
 */
public class BuyWrapper extends BaseControllerWrapper
{

    public BuyWrapper(Map<String, Object> single)
    {
        super(single);
    }

    public BuyWrapper(List<Map<String, Object>> multi)
    {
        super(multi);
    }

    public BuyWrapper(Page<Map<String, Object>> page)
    {
        super(page);
    }

    public BuyWrapper(PageResult<Map<String, Object>> pageResult)
    {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map)
    {
        Long memberId = (Long) map.get("memberId");
        map.put("memberIdValue", F.me().getMember(memberId).getAccount());


        String payMethod = (String) map.get("payMethod");
        if (payMethod != null)
        {
            for (ProConst.PayTypeEnum e : ProConst.PayTypeEnum.values())
            {
                if (e.code.equals(payMethod))
                {
                    map.put("payMethod", e.value);

                }
            }
        }
        String status = (String) map.get("status");
        map.put("statusValue", status!=null&&"Y".equals(status)?"是":"否");

    }
}