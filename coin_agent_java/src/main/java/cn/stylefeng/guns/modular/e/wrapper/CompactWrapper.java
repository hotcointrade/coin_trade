package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 合约订单Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-23 14:35:24
 */
public class CompactWrapper extends BaseControllerWrapper{

    public CompactWrapper(Map<String, Object> single) {
            super(single);
        }

        public CompactWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public CompactWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public CompactWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId = (Long) map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());


            String compactType = (String) map.get("compactType");
            if (compactType != null)
            {
                for (ProConst.CompactType e : ProConst.CompactType.values())
                {
                    if (e.code.equals(compactType))
                    {
                        map.put("compactType", e.value);

                    }
                }
            }

            String dealWay = (String) map.get("dealWay");
            if (dealWay != null)
            {
                for (ProConst.DealWay e : ProConst.DealWay.values())
                {
                    if (e.code.equals(dealWay))
                    {
                        map.put("dealWay", e.value);

                    }
                }
            }

            String exitType = (String) map.get("exitType");
            if (exitType != null)
            {
                for (ProConst.ExitType e : ProConst.ExitType.values())
                {
                    if (e.code.equals(exitType))
                    {
                        map.put("exitType", e.value);

                    }
                }
            }

            String status = (String) map.get("status");
            if (status != null) {
                for (ProConst.CompactStatus e : ProConst.CompactStatus.values()) {
                    if(e.code.equals(status))
                    {
                        map.put("statusValue",e.value);

                    }
                }
            }

//            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
//            BigDecimal leverRate = (BigDecimal) map.get("leverRate");

//            map.put("totalPrice",totalPrice.multiply(leverRate));


        }
}