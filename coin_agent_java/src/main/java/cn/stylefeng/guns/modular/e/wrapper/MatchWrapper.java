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
 * 委托单信息Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-18 10:38:28
 */
public class MatchWrapper extends BaseControllerWrapper{

    public MatchWrapper(Map<String, Object> single) {
            super(single);
        }

        public MatchWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MatchWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MatchWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId = (Long) map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());


            String matchType = (String) map.get("matchType");

            Object totalPrice=map.get("totalPrice");
            Object finishNumber=map.get("finishNumber");
            Object unFinishNumebr=map.get("unFinishNumebr");
            Object convertNumber=map.get("convertNumber");
            Object fee =  map.get("fee");
            map.put("fee",fee.toString());
            String symbols=(String)map.get("symbols");
            String[] coins=symbols.split("/");
            if (matchType != null)
            {
                for (ProConst.MatchType e : ProConst.MatchType.values())
                {
                    if (e.code.equals(matchType))
                    {
                        map.put("matchTypeValue", e.value);

                    }
                }
                String coin="";
                String convertCoin="";
                if(matchType.equals(ProConst.MatchType.BUY.code))
                {
                    coin=" "+coins[1];
                    convertCoin=" "+coins[0];
                }else {
                    coin=" "+coins[0];
                    convertCoin=" "+coins[1];
                }

                map.put("totalPrice",(totalPrice == null ? totalPrice : new BigDecimal(totalPrice+"").toPlainString())+coin);
                map.put("finishNumber",(finishNumber == null ? finishNumber : new BigDecimal(finishNumber+"").toPlainString())+coin);
                map.put("unFinishNumebr",(unFinishNumebr == null ? unFinishNumebr : new BigDecimal(unFinishNumebr+"").toPlainString())+coin);
                map.put("fee",(fee == null ? fee : new BigDecimal(fee+"").toPlainString())+coin);
                map.put("convertNumber",(convertNumber == null ? convertNumber : new BigDecimal(convertNumber+"").toPlainString())+convertCoin);
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
            String status = (String) map.get("status");
            if (status != null) {
                for (ProConst.MatchStatus e : ProConst.MatchStatus.values()) {
                    if(e.code.equals(status))
                    {
                        map.put("statusValue",e.value);

                    }
                }
            }


        }
}