package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 合约账户Map
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:09:49
 */
public class ContractMap extends AbstractDictMap {

   @Override
      public void init() {
          put("contractId","合约账户id");
      }

      @Override
      protected void initBeWrapped() {

      }
}