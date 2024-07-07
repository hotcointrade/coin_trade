package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 系统汇率Map
 *
 * @author yaying.liu
 * @since 2022-10-18 13:18:11
 */
public class PlatfomrRateMap extends AbstractDictMap {

   @Override
      public void init() {
          put("platfomrRateId","系统汇率id");
      }

      @Override
      protected void initBeWrapped() {

      }
}
