package cn.stylefeng.guns.modular.app.wrapper;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.com.entity.Region;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户信息Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-06 09:50:50
 */
public class MemberNextWrapper extends BaseControllerWrapper{

    public MemberNextWrapper(Map<String, Object> single) {
            super(single);
        }

        public MemberNextWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MemberNextWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MemberNextWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {


            Long refereeId=(Long)map.get("refereeId");
            if(refereeId!=null)
            {
                Member member = F.me().getMember(refereeId);
                map.put("refereeIdValue",member==null?"无":member.getAccount());
            }else{
                map.put("refereeIdValue","顶级账户");

            }

//            StringBuffer belongToWhere=new StringBuffer();
//            Long provinceId=(Long)map.get("provinceId");
//            if(provinceId!=null)
//            {
//               Region region= F.me().getRegion(provinceId);
//               map.put("provinceIdValue",region==null?"无":region.getName());
//                belongToWhere.append(region.getName());
//            }

//            Long cityId=(Long)map.get("cityId");
//            if(cityId!=null)
//            {
//               Region region= F.me().getRegion(cityId);
//               map.put("cityIdValue",region==null?"无":region.getName());
//                belongToWhere.append(region.getName());
//            }
//            Long areaId=(Long)map.get("areaId");
//            if(areaId!=null)
//            {
//               Region region= F.me().getRegion(areaId);
//               map.put("areaIdValue",region==null?"无":region.getName());
//                belongToWhere.append(region.getName());
//            }
//
//            map.put("BelongToWhere",belongToWhere);


//            String points=(String)map.get("points");
//            if(points!=null)
//            {
//                map.put("pointsValue",StrUtil.equals(points,"1")?"是":"否");
//            }
//            String provinceProxy=(String)map.get("provinceProxy");
//            if(provinceProxy!=null)
//            {
//                map.put("provinceProxyValue",StrUtil.equals(provinceProxy,"1")?"是":"否");
//            }
//            String cityProxy=(String)map.get("cityProxy");
//            if(cityProxy!=null)
//            {
//                map.put("cityProxyValue",StrUtil.equals(cityProxy,"1")?"是":"否");
//            }
//            String areaIdProxy=(String)map.get("areaIdProxy");
//            if(areaIdProxy!=null)
//            {
//                map.put("areaIdProxyValue",StrUtil.equals(areaIdProxy,"1")?"是":"否");
//            }

//            String director=(String)map.get("director");
//
//            if(director!=null)
//            {
//                map.put("directorValue",StrUtil.equals(director,"1")?"是":"否");
//            }
//            String cooperation=(String)map.get("cooperation");
//            if(cooperation!=null)
//            {
//                map.put("cooperationValue",StrUtil.equals(cooperation,"1")?"是":"否");
//            }
//
//            Long provinceProxyId=(Long)map.get("provinceProxyId");
//            if(provinceProxyId!=null)
//            {
//                Region region= F.me().getRegion(provinceProxyId);
//                map.put("provinceProxyIdValue",region==null?"无":region.getName());
//            }
//
//
//            Long cityProxyId=(Long)map.get("cityProxyId");
//            if(cityProxyId!=null)
//            {
//                Region region= F.me().getRegion(cityProxyId);
//                map.put("cityProxyIdValue",region==null?"无":region.getName());
//            }
//
//            Long areaIdProxyId=(Long)map.get("areaIdProxyId");
//            if(areaIdProxyId!=null)
//            {
//                Region region= F.me().getRegion(areaIdProxyId);
//                map.put("areaIdProxyIdValue",region==null?"无":region.getName());
//            }

            String status=(String)map.get("status");
            map.put("statusValue",status.equals("Y")?"启用":"禁用");

            Long memberId=(Long)map.get("memberId");

            BigDecimal todayWithdraw = F.me().getTodayWithdraw(memberId);
            map.put("todayWithdraw",todayWithdraw);
            BigDecimal todayRecharge = F.me().getTodayRecharge(memberId);
            map.put("todayRecharge",todayRecharge);
            BigDecimal allRecharge = F.me().getAllRecharge(memberId);
            map.put("allRecharge",allRecharge);
            BigDecimal allWithdraw = F.me().getAllWithdraw(memberId);
            map.put("allWithdraw",allWithdraw);
            int teamNumbers = F.me().getTeamNumbers(memberId);
            map.put("teamNumbers",teamNumbers);
            //冻结资产查询较久
            BigDecimal userNoPrice = F.me().getUserNoPrice(memberId);
            map.put("userNoPrice",userNoPrice==null?0:userNoPrice);



        }
}
