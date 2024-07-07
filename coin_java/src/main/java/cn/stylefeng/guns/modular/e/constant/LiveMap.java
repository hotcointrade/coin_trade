package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 生活支付开通记录Map
 *
 * @author yaying.liu
 * @Date 2020-06-28 16:17:00
 */
public class LiveMap extends AbstractDictMap {

   @Override
      public void init() {
          put("liveId","生活支付开通记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}