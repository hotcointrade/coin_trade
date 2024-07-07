package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 签到Map
 *
 * @author yaying.liu
 * @Date 2019-12-09 16:28:04
 */
public class CheckInMap extends AbstractDictMap {

   @Override
      public void init() {
          put("checkInId","签到id");
      }

      @Override
      protected void initBeWrapped() {

      }
}