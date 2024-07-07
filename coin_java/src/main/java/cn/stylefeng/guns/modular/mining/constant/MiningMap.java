package cn.stylefeng.guns.modular.mining.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 矿机Map
 *
 * @author yaying.liu
 * @since 2022-06-07 13:24:41
 */
public class MiningMap extends AbstractDictMap {

   @Override
      public void init() {
          put("miningId","矿机id");
      }

      @Override
      protected void initBeWrapped() {

      }
}