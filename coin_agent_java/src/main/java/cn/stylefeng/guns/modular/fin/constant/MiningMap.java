package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 矿机钱包Map
 *
 * @author yaying.liu
 * @since 2022-06-06 14:28:48
 */
public class MiningMap extends AbstractDictMap {

   @Override
      public void init() {
          put("miningId","矿机钱包id");
      }

      @Override
      protected void initBeWrapped() {

      }
}