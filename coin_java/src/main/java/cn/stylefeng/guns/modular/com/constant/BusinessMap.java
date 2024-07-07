package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 行业资讯Map
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:09:15
 */
public class BusinessMap extends AbstractDictMap {

   @Override
      public void init() {
          put("businessId","行业资讯id");
      }

      @Override
      protected void initBeWrapped() {

      }
}