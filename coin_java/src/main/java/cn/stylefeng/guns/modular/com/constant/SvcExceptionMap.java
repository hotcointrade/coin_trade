package cn.stylefeng.guns.modular.com.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 异常信息Map
 *
 * @author yaying.liu
 * @Date 2019-12-24 10:20:02
 */
public class SvcExceptionMap extends AbstractDictMap {

   @Override
      public void init() {
          put("svcExceptionId","异常信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}