package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 钱包历史变更Map
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:57
 */
public class WalletHistoryMap extends AbstractDictMap {

   @Override
      public void init() {
          put("walletHistoryId","钱包历史变更id");
      }

      @Override
      protected void initBeWrapped() {

      }
}