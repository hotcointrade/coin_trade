package cn.stylefeng.guns.modular.configLone.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.constant.MemberMap;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.app.wrapper.MemberWrapper;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.otc.entity.Buy;
import cn.stylefeng.guns.modular.otc.entity.Sell;
import cn.stylefeng.guns.modular.otc.service.BuyService;
import cn.stylefeng.guns.modular.otc.service.SellService;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户信息控制器
 *
 * @author yaying.liu
 * @Date 2019-12-06 09:50:49
 */
@Controller
@RequestMapping("/teamDetail")
public class TeamDetailController extends BaseController
{

    private String PREFIX = "/modular/team/teamDetail/";

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    @Autowired
    BuyService buyService;

    @Autowired
    SellService sellService;


    /**
     * 跳转到用户信息首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "member.html";
    }

    /**
     * 跳转到用户信息首页
     */
    @RequestMapping("/home")
    public String home()
    {
        return "/modular/home/home.html";
    }


    /**
     * 跳转到添加用户信息
     */
    @RequestMapping("/member_add")
    public String memberAdd()
    {
        return PREFIX + "member_add.html";
    }

    /**
     * 跳转到修改用户信息
     */
    @RequestMapping("/member_edit")
    public String memberEdit(Long memberId, Model model)
    {
        Member condition = this.memberService.getById(memberId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "member_edit.html";
    }

    /**
     * 用户信息详情
     */
    @RequestMapping(value = "/detail/{memberId}")
    @ResponseBody
    public Object detail(@PathVariable("memberId") Long memberId)
    {
        Member entity = memberService.getById(memberId);
        //  MemberDto conditionDto = new MemberDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询用户信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) Long refereeId,@RequestParam(required = false) Integer lever) {
        if (ShiroKit.isAdmin()){
            //根据条件查询
            Page<Map<String, Object>> result = memberService.selectByCondition(condition, refereeId,null,null);
            if(lever!=null) {
                List<Map<String, Object>> records = result.getRecords();
                List<Map<String, Object>> newList = new ArrayList<>();
                for (Map<String, Object> record : records) {
                    String parentRefereeId = (String) record.get("parentRefereeId");
                    String[] split = parentRefereeId.split("/");
                    if(lever == 1 && split.length==0){
                        newList.add(record);
                    }
                    if (split.length == lever) {
                        newList.add(record);
                    }
                }
                result.setRecords(newList);
            }
            Page wrapped = new MemberWrapper(result).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
        Long memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
        String recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        Page<Map<String, Object>> result = memberService.selectByCondition(condition, refereeId,memberId,recommendIds);
        Page wrapped = new MemberWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);

    }
    @RequestMapping("/tree")
    @ResponseBody
    public Object tree(@RequestParam(required = false) String condition, @RequestParam(required = false) Long refereeId) {
        if (refereeId == null){
            //根据条件查询
//            Page<Map<String, Object>> result = memberService.selectByCondition(condition, refereeId,null,null);
//            Page wrapped = new MemberWrapper(result).wrap();
            return LayuiPageFactory.createPageInfo(new MemberWrapper(new Page<>()).wrap());
        }
        Member m = memberService.getById(refereeId);
        User u = userService.getByAccount(m.getAccount());
        String recommendIds = F.me().getMemberRecommendIdsByUser(u.getUserId());
        Page<Map<String, Object>> result = memberService.selectByConditionForTeam(condition, refereeId,recommendIds);

        Page wrapped = new MemberWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);

    }


    /**
     * 获取关系链的tree列表，ztree格式
     */
    @RequestMapping(value = "/tree2")
    @ResponseBody
    public List<ZTreeNode> tree2()
    {
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
             memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
             recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        List<ZTreeNode> tree = this.memberService.tree(memberId,recommendIds);
//        tree.add(ZTreeNode.createParent());
        return tree;
    }


    /**
     * 修改状态
     *
     * @param status
     * @param memberId
     * @return
     */
    @RequestMapping("/status/{status}")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status, @RequestParam Long memberId)
    {
        Member entity = memberService.getById(memberId);
        entity.setStatus(status);
        memberService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 编辑用户信息
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "memberId", dict = MemberMap.class)
    @ResponseBody
    public ResponseData edit(Member member)
    {
        Member memberR = memberService.getById(member.getMemberId());
        if (!StrUtil.equals(memberR.getType(), member.getType()))
        {
            //类型不同，标记为修改为用户类型
            member.setState("1");
        }
        //标记为测试账号（TEST）  member.getRemark()

        memberService.updateById(member);
        return SUCCESS_TIP;
    }


    /**
     * 添加用户信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加用户信息", key = "name", dict = MemberMap.class)
    @ResponseBody
    public Result add(Member member, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (member == null)
        {
            return Result.fail(404, "参数不能为空");
        }
        return this.memberService.addMember(member);
    }

    /**
     * 删除用户信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除用户信息", key = "memberId", dict = MemberMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long memberId)
    {
        if (ToolUtil.isEmpty(memberId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.memberService.deleteMember(memberId);
        User user = new User();
        user.setMemberId(memberId);
        userService.remove(new QueryWrapper<>(user));

        Sell sell = new Sell();
        sell.setMemberId(memberId);
        sell.setDel("N");
        List<Sell> sellList = this.sellService.list(new QueryWrapper<>(sell));
        for (Sell sell1 :
                sellList) {
            sell1.setDel("Y");
            sell1.setUpdateTime(new Date());
            sell1.setUpdateUser(1l);
            this.sellService.updateById(sell1);
        }

        Buy buy = new Buy();
        buy.setMemberId(memberId);
        buy.setDel("N");
        List<Buy> buyList = this.buyService.list(new QueryWrapper<>(buy));
        for (Buy buy1:
             buyList) {
            buy1.setDel("Y");
            buy1.setUpdateTime(new Date());
            buy1.setUpdateUser(1l);
            this.buyService.updateById(buy1);
        }

        return SUCCESS_TIP;
    }

//    @RequestMapping("/getTypeList")
//    @ResponseBody
//    public List<Map<String, String>> getLevel() {
//
//        List<Node> list = nodeService.list();
//        List<Map<String,String>> result = new ArrayList<>();
//        for (Node entity : list) {
//            Map map = new HashMap();
//            map.put("code", entity.getType());
//            map.put("value","v"+ entity.getType());
//            result.add(map);
//        }
//        return result;
//    }





}
