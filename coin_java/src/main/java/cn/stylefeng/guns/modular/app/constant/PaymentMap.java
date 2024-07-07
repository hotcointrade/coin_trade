package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 收款方式Map
 *
 * @author yaying.liu
 * @Date 2020-03-12 11:23:03
 */
public class PaymentMap extends AbstractDictMap {

   @Override
      public void init() {
          put("paymentId","收款方式id");
      }

      @Override
      protected void initBeWrapped() {

      }
}