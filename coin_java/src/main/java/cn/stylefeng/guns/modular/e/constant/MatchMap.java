package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 委托单信息Map
 *
 * @author yaying.liu
 * @Date 2020-03-18 10:38:28
 */
public class MatchMap extends AbstractDictMap {

   @Override
      public void init() {
          put("matchId","委托单信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}