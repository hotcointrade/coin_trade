package cn.stylefeng.guns.modular.mining.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 矿机收益Map
 *
 * @author yaying.liu
 * @since 2022-06-07 13:36:19
 */
public class MiningOrderDetailMap extends AbstractDictMap {

   @Override
      public void init() {
          put("miningOrderDetailId","矿机收益id");
      }

      @Override
      protected void initBeWrapped() {

      }
}