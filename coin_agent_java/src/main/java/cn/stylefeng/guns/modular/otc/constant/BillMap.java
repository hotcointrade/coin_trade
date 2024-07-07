package cn.stylefeng.guns.modular.otc.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 交易结算订单Map
 *
 * @author yaying.liu
 * @since 2020-07-13 15:03:40
 */
public class BillMap extends AbstractDictMap {

   @Override
      public void init() {
          put("billId","交易结算订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}