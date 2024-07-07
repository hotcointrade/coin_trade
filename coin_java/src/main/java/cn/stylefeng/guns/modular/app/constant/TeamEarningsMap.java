package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 团队收益Map
 *
 * @author yaying.liu
 * @since 2022-02-18 20:22:22
 */
public class TeamEarningsMap extends AbstractDictMap {

   @Override
      public void init() {
          put("teamEarningsId","团队收益id");
      }

      @Override
      protected void initBeWrapped() {

      }
}