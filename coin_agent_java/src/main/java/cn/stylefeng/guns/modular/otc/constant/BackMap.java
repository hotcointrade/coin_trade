package cn.stylefeng.guns.modular.otc.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 退还押金Map
 *
 * @author yaying.liu
 * @since 2020-07-09 14:33:04
 */
public class BackMap extends AbstractDictMap {

   @Override
      public void init() {
          put("backId","退还押金id");
      }

      @Override
      protected void initBeWrapped() {

      }
}