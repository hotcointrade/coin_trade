package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 平台流水Map
 *
 * @author yaying.liu
 * @Date 2020-01-03 18:06:11
 */
public class PlatformCashflowMap extends AbstractDictMap {

   @Override
      public void init() {
          put("platformCashflowId","平台流水id");
      }

      @Override
      protected void initBeWrapped() {

      }
}