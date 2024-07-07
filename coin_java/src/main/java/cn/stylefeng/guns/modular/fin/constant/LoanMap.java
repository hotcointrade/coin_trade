package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 申请Map
 *
 * @author yaying.liu
 * @since 2022-10-18 20:42:53
 */
public class LoanMap extends AbstractDictMap {

   @Override
      public void init() {
          put("loanId","申请id");
      }

      @Override
      protected void initBeWrapped() {

      }
}