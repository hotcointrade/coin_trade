package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 转账信息Map
 *
 * @author yaying.liu
 * @Date 2020-01-14 17:08:46
 */
public class TransferMap extends AbstractDictMap {

   @Override
      public void init() {
          put("transferId","转账信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}