package cn.stylefeng.guns.modular.coin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 现货交易对Map
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
public class SpotMap extends AbstractDictMap {

   @Override
      public void init() {
          put("spotId","现货交易对id");
      }

      @Override
      protected void initBeWrapped() {

      }
}