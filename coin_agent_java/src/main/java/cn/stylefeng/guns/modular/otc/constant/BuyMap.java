package cn.stylefeng.guns.modular.otc.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 购买挂单订单Map
 *
 * @author yaying.liu
 * @since 2020-07-13 14:52:41
 */
public class BuyMap extends AbstractDictMap {

   @Override
      public void init() {
          put("buyId","购买挂单订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}