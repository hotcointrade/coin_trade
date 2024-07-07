package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 锁仓合约配置Map
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:07
 */
public class LockMap extends AbstractDictMap {

   @Override
      public void init() {
          put("lockId","锁仓合约配置id");
      }

      @Override
      protected void initBeWrapped() {

      }
}