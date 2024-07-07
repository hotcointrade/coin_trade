package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 团队返佣Map
 *
 * @author yaying.liu
 * @since 2022-08-22 14:46:40
 */
public class TeamLevelMap extends AbstractDictMap {

   @Override
      public void init() {
          put("teamLevelId","团队返佣id");
      }

      @Override
      protected void initBeWrapped() {

      }
}