package cn.stylefeng.guns.modular.otc.wrapper;

import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.ImgToList;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 交易结算订单Wrapper
 *
 * @author yaying.liu
 * @since 2020-07-13 15:03:40
 */
public class BillWrapper extends BaseControllerWrapper
{

    public BillWrapper(Map<String, Object> single)
    {
        super(single);
    }

    public BillWrapper(List<Map<String, Object>> multi)
    {
        super(multi);
    }

    public BillWrapper(Page<Map<String, Object>> page)
    {
        super(page);
    }

    public BillWrapper(PageResult<Map<String, Object>> pageResult)
    {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map)
    {
        Long buyMid = (Long) map.get("buyMid");
        map.put("buyMidValue", F.me().getMember(buyMid).getAccount());
        Long sellMid = (Long) map.get("sellMid");
        map.put("sellMidValue", F.me().getMember(sellMid).getAccount());


        String payMethod = (String) map.get("payMethod");
        if (payMethod != null)
        {
            for (ProConst.PayTypeEnum e : ProConst.PayTypeEnum.values())
            {
                if (e.code.equals(payMethod))
                {
                    map.put("payMethod", e.value);
                    continue;
                }
            }
        }
        String status = (String) map.get("status");
        if (status != null)
        {
            for (ProConst.BillStatus e : ProConst.BillStatus.values())
            {
                if (e.code.equals(status))
                {
                    map.put("statusValue", e.value);
                    continue;
                }
            }
        }

         String appealStatus = (String) map.get("appealStatus");
        if (appealStatus != null)
        {
            for (ProConst.AppealStatus e : ProConst.AppealStatus.values())
            {
                if (e.code.equals(appealStatus))
                {
                    map.put("appealStatusValue", e.value);
                    continue;
                }
            }
        }


        String appealType=(String)map.get("appealType");
        if(appealType!=null)
        {
            map.put("appealTypeValue","BUY".equals(appealType)?"买方":"卖方");
        }

        String duty = (String) map.get("duty");
        if (duty != null)
        {
            for (ProConst.DutyType e : ProConst.DutyType.values())
            {
                if (e.code.equals(duty))
                {
                    map.put("dutyValue", e.value);
                    continue;
                }
            }
        }
        //图片
        String img1 = (String) map.get("img1");

        List<Map<String, Object>> list1 = ImgToList.toList(img1);

        List<String> imgList1=new ArrayList<>();
        if(null!=list1)
        {
            for (Map<String,Object> p:list1) {
                imgList1.add((String)p.get("src"));
            }
        }
        map.put("imgs1",imgList1);
        //图片
        String img = (String) map.get("img");

        List<Map<String, Object>> list = ImgToList.toList(img);

        List<String> imgList=new ArrayList<>();
        if(null!=list)
        {
            for (Map<String,Object> p:list) {
                imgList.add((String)p.get("src"));
            }
        }
        map.put("imgs",imgList);

    }
}