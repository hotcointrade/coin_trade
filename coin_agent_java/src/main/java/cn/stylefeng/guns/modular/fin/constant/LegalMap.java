package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 法币账户Map
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:11:25
 */
public class LegalMap extends AbstractDictMap {

   @Override
      public void init() {
          put("legalId","法币账户id");
      }

      @Override
      protected void initBeWrapped() {

      }
}