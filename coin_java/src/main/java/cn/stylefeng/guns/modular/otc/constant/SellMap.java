package cn.stylefeng.guns.modular.otc.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 出售挂单订单Map
 *
 * @author yaying.liu
 * @since 2020-07-13 14:53:27
 */
public class SellMap extends AbstractDictMap {

   @Override
      public void init() {
          put("sellId","出售挂单订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}