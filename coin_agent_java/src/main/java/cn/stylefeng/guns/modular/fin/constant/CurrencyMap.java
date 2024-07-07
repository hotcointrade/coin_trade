package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 币币账户Map
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
public class CurrencyMap extends AbstractDictMap {

   @Override
      public void init() {
          put("currencyId","币币账户id");
      }

      @Override
      protected void initBeWrapped() {

      }
}