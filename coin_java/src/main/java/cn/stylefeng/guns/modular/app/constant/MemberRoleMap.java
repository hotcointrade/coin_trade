package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 用户角色关系Map
 *
 * @author yaying.liu
 * @Date 2019-12-06 10:18:16
 */
public class MemberRoleMap extends AbstractDictMap {

   @Override
      public void init() {
          put("memberRoleId","用户角色关系id");
      }

      @Override
      protected void initBeWrapped() {

      }
}