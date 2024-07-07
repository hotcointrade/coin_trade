package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 钱包调用api记录Map
 *
 * @author yaying.liu
 * @since 2020-08-18 19:46:56
 */
public class DappMap extends AbstractDictMap {

   @Override
      public void init() {
          put("dappId","钱包调用api记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}