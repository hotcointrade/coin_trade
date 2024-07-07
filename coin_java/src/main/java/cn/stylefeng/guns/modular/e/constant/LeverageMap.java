package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 杠杆倍率Map
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
public class LeverageMap extends AbstractDictMap {

   @Override
      public void init() {
          put("leverageId","杠杆倍率id");
      }

      @Override
      protected void initBeWrapped() {

      }
}