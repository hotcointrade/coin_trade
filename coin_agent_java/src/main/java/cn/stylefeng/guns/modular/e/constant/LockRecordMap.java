package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 锁仓记录Map
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:59
 */
public class LockRecordMap extends AbstractDictMap {

   @Override
      public void init() {
          put("lockRecordId","锁仓记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}