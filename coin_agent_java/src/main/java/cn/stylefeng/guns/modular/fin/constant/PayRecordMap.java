package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 第三方接口记录表Map
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:15:58
 */
public class PayRecordMap extends AbstractDictMap {

   @Override
      public void init() {
          put("payRecordId","第三方接口记录表id");
      }

      @Override
      protected void initBeWrapped() {

      }
}