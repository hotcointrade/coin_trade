package cn.stylefeng.guns.modular.chain.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 豆兑换信息Map
 *
 * @author yaying.liu
 * @Date 2020-01-17 14:46:22
 */
public class ConvertMap extends AbstractDictMap {

   @Override
      public void init() {
          put("convertId","豆兑换信息id");
      }

      @Override
      protected void initBeWrapped() {

      }
}