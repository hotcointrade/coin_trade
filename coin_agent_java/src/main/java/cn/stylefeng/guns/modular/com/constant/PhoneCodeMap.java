package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 电话区号Map
 *
 * @author yaying.liu
 * @Date 2019-12-20 17:59:48
 */
public class PhoneCodeMap extends AbstractDictMap {

   @Override
      public void init() {
          put("phoneCodeId","电话区号id");
      }

      @Override
      protected void initBeWrapped() {

      }
}