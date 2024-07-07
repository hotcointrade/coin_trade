package cn.stylefeng.guns.modular.e.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 撮合交易成交明细Map
 *
 * @author yaying.liu
 * @Date 2020-05-20 10:29:28
 */
public class MatchDetailMap extends AbstractDictMap
{

   @Override
      public void init() {
          put("matchDetailId","撮合交易成交明细id");
      }

      @Override
      protected void initBeWrapped() {

      }
}