package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 提币信息Map
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:05:21
 */
public class WithdrawMap extends AbstractDictMap {

   @Override
      public void init() {
          put("withdrawId","提币信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}