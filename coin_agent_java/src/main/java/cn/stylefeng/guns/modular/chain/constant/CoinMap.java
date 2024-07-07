package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 用户钱包地址Map
 *
 * @author yaying.liu
 * @Date 2019-12-10 18:24:36
 */
public class CoinMap extends AbstractDictMap {

   @Override
      public void init() {
          put("coinId","用户钱包地址id");
      }

      @Override
      protected void initBeWrapped() {

      }
}