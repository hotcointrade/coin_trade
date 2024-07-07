package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 视频和音频Map
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:44:00
 */
public class MediaMap extends AbstractDictMap {

   @Override
      public void init() {
          put("mediaId","视频和音频id");
      }

      @Override
      protected void initBeWrapped() {

      }
}