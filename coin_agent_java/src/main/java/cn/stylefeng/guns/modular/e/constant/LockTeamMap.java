package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 锁仓团队奖配置Map
 *
 * @author yaying.liu
 * @since 2020-09-25 10:45:37
 */
public class LockTeamMap extends AbstractDictMap {

   @Override
      public void init() {
          put("lockTeamId","锁仓团队奖配置id");
      }

      @Override
      protected void initBeWrapped() {

      }
}