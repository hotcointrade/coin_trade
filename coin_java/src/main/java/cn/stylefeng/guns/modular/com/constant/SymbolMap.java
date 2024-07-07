package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 币种Map
 *
 * @author yaying.liu
 * @Date 2020-03-09 11:03:53
 */
public class SymbolMap extends AbstractDictMap {

   @Override
      public void init() {
          put("symbolId","币种id");
      }

      @Override
      protected void initBeWrapped() {

      }
}