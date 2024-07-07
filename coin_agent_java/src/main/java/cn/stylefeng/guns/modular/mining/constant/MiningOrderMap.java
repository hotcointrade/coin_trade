package cn.stylefeng.guns.modular.mining.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 矿机订单Map
 *
 * @author yaying.liu
 * @since 2022-06-08 21:02:12
 */
public class MiningOrderMap extends AbstractDictMap {

   @Override
      public void init() {
          put("miningOrderId","矿机订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}