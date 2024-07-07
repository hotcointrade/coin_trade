package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 公账提币Map
 *
 * @author yaying.liu
 * @Date 2020-01-17 18:55:07
 */
public class PublicMap extends AbstractDictMap {

   @Override
      public void init() {
          put("publicId","公账提币id");
      }

      @Override
      protected void initBeWrapped() {

      }
}