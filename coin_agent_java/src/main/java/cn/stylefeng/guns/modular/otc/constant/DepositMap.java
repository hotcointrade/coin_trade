package cn.stylefeng.guns.modular.otc.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 用户押金Map
 *
 * @author yaying.liu
 * @since 2020-07-01 11:55:46
 */
public class DepositMap extends AbstractDictMap {

   @Override
      public void init() {
          put("depositId","用户押金id");
      }

      @Override
      protected void initBeWrapped() {

      }
}