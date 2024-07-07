package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 充币记录Map
 *
 * @author yaying.liu
 * @Date 2020-01-14 16:00:54
 */
public class RechargeMap extends AbstractDictMap {

   @Override
      public void init() {
          put("rechargeId","充币记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}