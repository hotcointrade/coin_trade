package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 支付订单Map
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:14:16
 */
public class PayOrderMap extends AbstractDictMap {

   @Override
      public void init() {
          put("payOrderId","支付订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}