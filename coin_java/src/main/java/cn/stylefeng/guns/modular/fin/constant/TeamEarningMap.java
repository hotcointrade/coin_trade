package cn.stylefeng.guns.modular.fin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 团队佣金Map
 *
 * @author yaying.liu
 * @since 2022-02-18 18:53:54
 */
public class TeamEarningMap extends AbstractDictMap {

   @Override
      public void init() {
          put("teamEarningId","团队佣金id");
      }

      @Override
      protected void initBeWrapped() {

      }
}