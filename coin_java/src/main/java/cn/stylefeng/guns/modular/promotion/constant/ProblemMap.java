package cn.stylefeng.guns.modular.promotion.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 常见问题Map
 *
 * @author yaying.liu
 * @Date 2019-10-14 14:11:51
 */
public class ProblemMap extends AbstractDictMap {

   @Override
      public void init() {
          put("problemId","常见问题id");
      }

      @Override
      protected void initBeWrapped() {

      }
}