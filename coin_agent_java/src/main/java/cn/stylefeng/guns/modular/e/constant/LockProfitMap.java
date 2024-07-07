package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 锁仓静态收益记录Map
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:24
 */
public class LockProfitMap extends AbstractDictMap {

   @Override
      public void init() {
          put("lockProfitId","锁仓静态收益记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}