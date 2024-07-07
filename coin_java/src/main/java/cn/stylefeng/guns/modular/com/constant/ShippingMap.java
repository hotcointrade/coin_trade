package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 收货地址Map
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:11:22
 */
public class ShippingMap extends AbstractDictMap {

   @Override
      public void init() {
          put("shippingId","收货地址id");
      }

      @Override
      protected void initBeWrapped() {

      }
}