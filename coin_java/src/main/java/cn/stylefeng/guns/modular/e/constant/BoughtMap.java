package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 法币交易Map
 *
 * @author yaying.liu
 * @Date 2020-04-03 09:48:13
 */
public class BoughtMap extends AbstractDictMap {

   @Override
      public void init() {
          put("boughtId","法币交易id");
      }

      @Override
      protected void initBeWrapped() {

      }
}