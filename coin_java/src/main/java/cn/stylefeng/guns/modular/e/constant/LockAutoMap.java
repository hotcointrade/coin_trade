package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 锁仓动态收益配置Map
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:59
 */
public class LockAutoMap extends AbstractDictMap {

   @Override
      public void init() {
          put("lockAutoId","锁仓动态收益配置id");
      }

      @Override
      protected void initBeWrapped() {

      }
}