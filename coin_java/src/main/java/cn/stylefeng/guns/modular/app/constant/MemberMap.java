package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 用户信息Map
 *
 * @author yaying.liu
 * @Date 2019-12-06 09:50:50
 */
public class MemberMap extends AbstractDictMap {

   @Override
      public void init() {
          put("memberId","用户信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}