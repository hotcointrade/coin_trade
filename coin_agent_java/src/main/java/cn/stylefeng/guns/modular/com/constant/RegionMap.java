package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 地区Map
 *
 * @author yaying.liu
 * @Date 2019-12-06 11:32:41
 */
public class RegionMap extends AbstractDictMap {

   @Override
      public void init() {
          put("regionId","地区id");
      }

      @Override
      protected void initBeWrapped() {

      }
}