package cn.stylefeng.guns.modular.promotion.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 网关定义Map
 *
 * @author yaying.liu
 * @Date 2019-10-11 11:20:42
 */
public class GatewayDefineMap extends AbstractDictMap {

   @Override
      public void init() {
          put("gatewayDefineId","网关定义id");
      }

      @Override
      protected void initBeWrapped() {

      }
}