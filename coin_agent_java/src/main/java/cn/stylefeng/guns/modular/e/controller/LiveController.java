package cn.stylefeng.guns.modular.e.controller;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.roses.core.base.controller.BaseController;
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
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.e.service.LiveService;
import cn.stylefeng.guns.modular.e.wrapper.LiveWrapper;
import cn.stylefeng.guns.modular.e.entity.Live;
import cn.stylefeng.guns.modular.e.constant.LiveMap;

/**
 * 生活支付开通记录控制器
 *
 * @author yaying.liu
 * @Date 2020-06-28 16:17:00
 */
@Controller
@RequestMapping("/live")
public class LiveController extends BaseController
{

    private String PREFIX = "/modular/e/live/";

    @Autowired
    private LiveService liveService;
    @Autowired
    private MemberService memberService;

    /**
     * 跳转到生活支付开通记录首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "live.html";
    }

    /**
     * 跳转到添加生活支付开通记录
     */
    @RequestMapping("/live_add")
    public String liveAdd()
    {
        return PREFIX + "live_add.html";
    }

    /**
     * 跳转到修改生活支付开通记录
     */
    @RequestMapping("/live_edit")
    public String liveEdit(Long liveId, Model model)
    {
        Live condition = this.liveService.getById(liveId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "live_edit.html";
    }

    /**
     * 生活支付开通记录详情
     */
    @RequestMapping(value = "/detail/{liveId}")
    @ResponseBody
    public Object detail(@PathVariable("liveId") Long liveId)
    {
        Live entity = liveService.getById(liveId);
        //  LiveDto conditionDto = new LiveDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }

    /**
     * 修改状态
     *
     * @param status
     * @param id
     * @return
     */
    @RequestMapping("/status/{status}")
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status, @RequestParam Long id)
    {
        Live entity = liveService.getById(id);
        entity.setStatus(status);
        liveService.updateById(entity);

        Member member = memberService.getById(entity.getMemberId());
        member.setLive("E").setUpdateUser(Constant.SYS_PLATFORM);
        memberService.updateById(member);
        return SUCCESS_TIP;
    }

    /**
     * 查询生活支付开通记录列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition)
    {
        //根据条件查询
        Page<Map<String, Object>> result = liveService.selectByCondition(condition);
        Page wrapped = new LiveWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑生活支付开通记录
     */
    @RequestMapping("/edit")
    // @BussinessLog(value = "编辑参数", key = "liveId", dict = LiveMap.class)
    @ResponseBody
    public ResponseData edit(Live live)
    {
        liveService.updateById(live);
        return SUCCESS_TIP;
    }

    /**
     * 添加生活支付开通记录
     */
    @RequestMapping("/add")
    // @BussinessLog(value = "添加生活支付开通记录", key = "name", dict = LiveMap.class)
    @ResponseBody
    public ResponseData add(Live live, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (live == null)
        {
            return ResponseData.error("参数不能为空");
        }
        this.liveService.addLive(live);
        return SUCCESS_TIP;
    }

    /**
     * 删除生活支付开通记录
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除生活支付开通记录", key = "liveId", dict = LiveMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long liveId)
    {
        if (ToolUtil.isEmpty(liveId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.liveService.deleteLive(liveId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{liveId}")
    @ResponseBody
    public Object content(@PathVariable("liveId") Long id)
    {
        Live live = liveService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(live);
        return super.warpObject(new LiveWrapper(stringObjectMap));
    }
}