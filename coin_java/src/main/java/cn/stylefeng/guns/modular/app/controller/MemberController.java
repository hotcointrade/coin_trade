package cn.stylefeng.guns.modular.app.controller;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.constant.dictmap.UserDict;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.service.DeleteService;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.app.wrapper.MemberNextWrapper;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.otc.entity.Buy;
import cn.stylefeng.guns.modular.otc.entity.Sell;
import cn.stylefeng.guns.modular.otc.service.BuyService;
import cn.stylefeng.guns.modular.otc.service.SellService;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.app.wrapper.MemberWrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.constant.MemberMap;

/**
 * 用户信息控制器
 *
 * @author yaying.liu
 * @Date 2019-12-06 09:50:49
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {
	
	private String PREFIX = "/modular/app/member/";
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	BuyService buyService;
	
	@Autowired
	SellService sellService;
	
	/**
	 * 跳转到用户信息首页
	 */
	@RequestMapping("/chatConfig")
	public String chatConfig() {
		return "/modular/chat/chatMemberManager/member.html";
	}
	
	/**
	 * 跳转到用户信息首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "member.html";
	}
	
	/**
	 * 跳转到用户信息首页
	 */
	@RequestMapping("/home")
	public String home() {
		return "/modular/home/home.html";
	}
	
	/**
	 * 跳转到添加用户信息
	 */
	@RequestMapping("/member_add")
	public String memberAdd() {
		return PREFIX + "member_add.html";
	}
	
	/**
	 * 跳转到修改用户信息
	 */
	@RequestMapping("/member_edit")
	public String memberEdit(Long memberId, Model model) {
		Member condition = this.memberService.getById(memberId);
		model.addAllAttributes(BeanUtil.beanToMap(condition));
		LogObjectHolder.me().set(condition);
		return PREFIX + "member_edit.html";
	}
	
	/**
	 * 跳转到用户团队信息
	 */
	@RequestMapping("/member_team_info")
	public String member_team_info(Long memberId, Model model) {
//        HashMap map = this.memberService.myTeamInfo(memberId);
//        model.addAllAttributes(map);
//        LogObjectHolder.me().set(map);
		return PREFIX + "member_team_info.html";
	}
	
	/**
	 * 用户团队信息
	 */
	@RequestMapping("/memberTeamInfo/{memberId}")
	@ResponseBody
	public Object memberTeamInfo(@PathVariable("memberId") Long memberId) {
		HashMap map = this.memberService.myTeamInfo(memberId);
		return map;
	}
	
	/**
	 * 用户资产信息
	 */
	@RequestMapping("/memberAllWallet/{memberId}")
	@ResponseBody
	public Object memberAllWallet(@PathVariable("memberId") Long memberId) {
		HashMap map = this.memberService.memberAllWallet(memberId);
		return map;
	}
	
	/**
	 * 用户信息详情
	 */
	@RequestMapping(value = "/detail/{memberId}")
	@ResponseBody
	public Object detail(@PathVariable("memberId") Long memberId) {
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
	public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String uid, @RequestParam(required = false) Long refereeId) {
		if (ShiroKit.isAdmin()) {
			//根据条件查询
			Page<Map<String, Object>> result = memberService.selectByCondition(condition, refereeId, null, null, uid);
			Page wrapped = new MemberWrapper(result).wrap();
			return LayuiPageFactory.createPageInfo(wrapped);
		}
		Long memberId = F.me().getMemberByUser(ShiroKit.getUser().getId());
		String recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
		Page<Map<String, Object>> result = memberService.selectByCondition(condition, refereeId, memberId, recommendIds, uid);
		Page wrapped = new MemberWrapper(result).wrap();
		return LayuiPageFactory.createPageInfo(wrapped);
		
	}
	
	@RequestMapping("/list2")
	@ResponseBody
	public Object list2(@RequestParam(required = false) String condition, @RequestParam(required = true) Long refereeId, @RequestParam(required = true, defaultValue = "1") String level) {
		
		String recommendIds = F.me().getMemberRecommendIdsByMember(refereeId);
		if ("1".equals(level)) {
			ArrayList<Map> members = new ArrayList<>(1);
			Member member1 = new Member();
			member1.setMemberId(refereeId);
			Map<String, Object> map = memberService.selectById(refereeId);
//            Member member = this.memberService.getById(refereeId);
			members.add(map);
			Page page = LayuiPageFactory.defaultPage();
			page.setTotal(members.size());
			page.setRecords(members);
			Page wrapped = new MemberNextWrapper(page).wrap();
			return LayuiPageFactory.createPageInfo(wrapped);
			
		} else if ("2".equals(level)) {
			Page<Map<String, Object>> result = memberService.selectByCondition(condition, refereeId, null, null);
			Page wrapped = new MemberNextWrapper(result).wrap();
			return LayuiPageFactory.createPageInfo(wrapped);
		} else if ("3".equals(level)) {
			Page<Map<String, Object>> result = memberService.selectByLevel3(refereeId);
			Page wrapped = new MemberNextWrapper(result).wrap();
			return LayuiPageFactory.createPageInfo(wrapped);
			
		} else if ("4".equals(level)) {
			Page<Map<String, Object>> result = memberService.selectByLevel4(refereeId, recommendIds);
			Page wrapped = new MemberNextWrapper(result).wrap();
			return LayuiPageFactory.createPageInfo(wrapped);
			
		}
		
		return LayuiPageFactory.createPageInfo(LayuiPageFactory.defaultPage());
		
	}
	
	/**
	 * 获取关系链的tree列表，ztree格式
	 */
	@RequestMapping(value = "/tree")
	@ResponseBody
	public List<ZTreeNode> tree() {
		Long memberId = null;
		String recommendIds = null;
		if (!ShiroKit.isAdmin()) {
			memberId = F.me().getMemberByUser(ShiroKit.getUser().getId());
			recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
		}
		List<ZTreeNode> tree = this.memberService.tree(memberId, recommendIds);
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
	public ResponseData status(@PathVariable("status") String status, @RequestParam Long memberId) {
		Member entity = memberService.getById(memberId);
		entity.setStatus(status);
		memberService.updateById(entity);
		Object o = redisUtil.get(Constant.SINGLE_ACCOUNT + entity.getAccount());
		if (o != null) {
			String token = (String) o;
			redisUtil.set(token, entity, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 修改状态
	 *
	 * @param status
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/chatStatus/{status}")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public ResponseData chatStatus(@PathVariable("status") String status, @RequestParam Long memberId) {
		Member entity = memberService.getById(memberId);
		entity.setChatStatus(status);
		memberService.updateById(entity);
		Object o = redisUtil.get(Constant.SINGLE_ACCOUNT + entity.getAccount());
		if (o != null) {
			String token = (String) o;
			redisUtil.set(token, entity, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 重置用户资金密码
	 *
	 * @param
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/updatePay/{memberId}")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public ResponseData status(@PathVariable("memberId") String memberId) {
//        if (ToolUtil.isEmpty(memberId)) {
//            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
//        }
		Member member = memberService.getById(memberId);
		String newSalt = ShiroKit.getRandomSalt(5);
		String newPwdmd5 = ShiroKit.md5(Const.DEFAULT_PAY_PWD, newSalt);
		member.setPayPassword(newPwdmd5).setPaySalt(newSalt).setUpdateUser(member.getMemberId());
		member.setIsOpenPay("Y");
		memberService.updateById(member);
		Object o = redisUtil.get(Constant.SINGLE_ACCOUNT + member.getAccount());
		if (o != null) {
			String token = (String) o;
			redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 重置用户资金密码
	 */
//    @RequestMapping("/updatePay2")
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    private ResponseData updatePay2(@RequestParam Long memberId){
//        if (ToolUtil.isEmpty(memberId)) {
//            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
//        }
//        Member member = memberService.getById(memberId);
//        String newSalt = ShiroKit.getRandomSalt(5);
//        String newPwdmd5 = ShiroKit.md5(Const.DEFAULT_PAY_PWD, newSalt);
//        member.setPayPassword(newPwdmd5).setPaySalt(newSalt).setUpdateUser(member.getMemberId());
//        member.setIsOpenPay("Y");
//        memberService.updateById(member);
//        //redisUtil.del(Constant.SMS + member.getAccount());
//        return SUCCESS_TIP;
//    }
	
	/**
	 * 编辑用户信息
	 */
	@RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "memberId", dict = MemberMap.class)
	@ResponseBody
	public ResponseData edit(Member member) {
		Member memberR = memberService.getById(member.getMemberId());
		if (!StrUtil.equals(memberR.getType(), member.getType())) {
			//类型不同，标记为修改为用户类型
			member.setState("1");
		}
		//标记为测试账号（TEST）  member.getRemark()
		
		memberService.updateById(member);
		Object o = redisUtil.get(Constant.SINGLE_ACCOUNT + member.getAccount());
		if (o != null) {
			String token = (String) o;
			redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 添加用户信息
	 */
	@RequestMapping("/add")
	@BussinessLog(value = "添加用户信息", key = "name", dict = MemberMap.class)
	@ResponseBody
	public Result add(Member member, BindingResult result) {
		if (result.hasErrors()) {
			throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
		}
		if (member == null) {
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
	public ResponseData delete(@RequestParam Long memberId) {
		if (ToolUtil.isEmpty(memberId)) {
			throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
		}
		this.memberService.deleteMember(memberId);
		User user = new User();
		user.setMemberId(memberId);
		this.userService.deleteUser(userService.getOne(new QueryWrapper<>(user)).getUserId());
		
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
		for (Buy buy1 :
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
