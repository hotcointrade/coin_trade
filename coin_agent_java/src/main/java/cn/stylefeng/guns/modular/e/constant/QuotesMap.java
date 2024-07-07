package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 行情涨跌控制Map
 *
 * @author yaying.liu
 * @Date 2020-03-23 11:44:38
 */
public class QuotesMap extends AbstractDictMap {

   @Override
      public void init() {
          put("quotesId","行情涨跌控制id");
      }

      @Override
      protected void initBeWrapped() {

      }
}