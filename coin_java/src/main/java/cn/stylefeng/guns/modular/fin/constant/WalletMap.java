package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 账户信息Map
 *
 * @author yaying.liu
 * @Date 2020-03-12 17:05:14
 */
public class WalletMap extends AbstractDictMap {

   @Override
      public void init() {
          put("walletId","账户信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}