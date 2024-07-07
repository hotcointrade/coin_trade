package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 平台地址Map
 *
 * @author yaying.liu
 * @Date 2020-03-16 09:31:12
 */
public class PlatformAddressMap extends AbstractDictMap {

   @Override
      public void init() {
          put("platformAddressId","平台地址id");
          put("address","地址");
      }

      @Override
      protected void initBeWrapped() {

      }
}