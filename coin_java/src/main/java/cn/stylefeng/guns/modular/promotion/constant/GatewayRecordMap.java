package cn.stylefeng.guns.modular.promotion.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 网关记录Map
 *
 * @author yaying.liu
 * @Date 2019-10-11 10:44:44
 */
public class GatewayRecordMap extends AbstractDictMap {

   @Override
      public void init() {
          put("gatewayRecordId","网关记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}