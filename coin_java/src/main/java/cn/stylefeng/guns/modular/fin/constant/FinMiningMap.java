package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 矿机账户Map
 *
 * @author yaying.liu
 * @since 2022-06-07 13:37:29
 */
public class FinMiningMap extends AbstractDictMap {

   @Override
      public void init() {
          put("finMiningId","矿机账户id");
      }

      @Override
      protected void initBeWrapped() {

      }
}