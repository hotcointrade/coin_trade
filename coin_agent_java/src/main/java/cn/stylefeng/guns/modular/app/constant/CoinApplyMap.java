package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 上币申请Map
 *
 * @author yaying.liu
 * @since 2022-02-20 20:37:01
 */
public class CoinApplyMap extends AbstractDictMap {

   @Override
      public void init() {
          put("coinApplyId","上币申请id");
      }

      @Override
      protected void initBeWrapped() {

      }
}