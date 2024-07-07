package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 合约订单Map
 *
 * @author yaying.liu
 * @Date 2020-03-23 14:35:24
 */
public class CompactMap extends AbstractDictMap {

   @Override
      public void init() {
          put("compactId","合约订单id");
      }

      @Override
      protected void initBeWrapped() {

      }
}