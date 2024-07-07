package cn.stylefeng.guns.modular.coin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 合约交易对Map
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
public class SwapMap extends AbstractDictMap {

   @Override
      public void init() {
          put("swapId","合约交易对id");
      }

      @Override
      protected void initBeWrapped() {

      }
}