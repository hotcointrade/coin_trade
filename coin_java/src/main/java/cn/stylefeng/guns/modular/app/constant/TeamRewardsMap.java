package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 社区奖励Map
 *
 * @author yaying.liu
 * @since 2022-07-07 17:45:57
 */
public class TeamRewardsMap extends AbstractDictMap {

   @Override
      public void init() {
          put("teamRewardsId","社区奖励id");
      }

      @Override
      protected void initBeWrapped() {

      }
}