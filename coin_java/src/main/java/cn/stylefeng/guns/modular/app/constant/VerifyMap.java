package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 实名认证Map
 *
 * @author yaying.liu
 * @Date 2020-03-11 16:31:58
 */
public class VerifyMap extends AbstractDictMap {

   @Override
      public void init() {
          put("verifyId","实名认证id");
      }

      @Override
      protected void initBeWrapped() {

      }
}