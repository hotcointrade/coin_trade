package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 佣金划转Map
 *
 * @author yaying.liu
 * @since 2022-02-18 20:54:40
 */
public class WalletTransferMap extends AbstractDictMap {

   @Override
      public void init() {
          put("walletTransferId","佣金划转id");
      }

      @Override
      protected void initBeWrapped() {

      }
}