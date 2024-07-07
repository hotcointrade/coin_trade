package cn.stylefeng.guns.modular.app.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 官方社区Map
 *
 * @author yaying.liu
 * @since 2022-10-18 18:24:14
 */
public class CommunityMap extends AbstractDictMap {

   @Override
      public void init() {
          put("communityId","官方社区id");
      }

      @Override
      protected void initBeWrapped() {

      }
}