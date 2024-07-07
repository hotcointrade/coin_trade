package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 生活支付打款Map
 *
 * @author yaying.liu
 * @Date 2020-06-29 17:01:20
 */
public class LiveRecordMap extends AbstractDictMap {

   @Override
      public void init() {
          put("liveRecordId","生活支付打款id");
      }

      @Override
      protected void initBeWrapped() {

      }
}