package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 流水记录Map
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:14
 */
public class CashflowMap extends AbstractDictMap {

   @Override
      public void init() {
          put("cashflowId","流水记录id");
      }

      @Override
      protected void initBeWrapped() {

      }
}