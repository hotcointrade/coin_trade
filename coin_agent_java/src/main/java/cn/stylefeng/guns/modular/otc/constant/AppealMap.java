package cn.stylefeng.guns.modular.otc.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 申诉订单Map
 *
 * @author yaying.liu
 * @since 2020-07-15 17:16:25
 */
public class AppealMap extends AbstractDictMap {

   @Override
      public void init() {
          put("appealId","申诉订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}