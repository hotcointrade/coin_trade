package cn.stylefeng.guns.modular.app.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.constant.PaymentMap;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.entity.Payment;
import cn.stylefeng.guns.modular.app.entity.RobotConfig;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.app.service.PaymentService;
import cn.stylefeng.guns.modular.app.service.RobotConfigService;
import cn.stylefeng.guns.modular.app.wrapper.PaymentWrapper;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.meta_data.constant.ConfigMap;
import cn.stylefeng.guns.modular.meta_data.entity.Config;
import cn.stylefeng.guns.modular.meta_data.model.ConfigDto;
import cn.stylefeng.guns.modular.meta_data.service.ConfigService;
import cn.stylefeng.guns.modular.meta_data.wrapper.ConfigWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 收款方式控制器
 *
 * @author yaying.liu
 * @Date 2020-03-12 11:23:03
 */
@Controller
@RequestMapping("/robot")
public class RobotConfigController extends BaseController {

    private String PREFIX = "/modular/system/robot/";


    //private static String PREFIX = "/modular/bulletin/email_config/";
    @Autowired
    private MemberService memberService;


    @Autowired
    private RobotConfigService robotConfigService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 跳转到管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "robot_config.html";
    }


    /**
     * 跳转到添加
     */
    @RequestMapping("/config_add")
    public String configAdd() {
        return PREFIX + "robot_config_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/config_edit")
    public String configEdit(Long configId, Model model) {
        RobotConfig condition = this.robotConfigService.getById(configId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX+"robot_config_edit.html";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{configId}")
    @ResponseBody
    public Object detail(@PathVariable("configId") Long configId) {
        RobotConfig config = robotConfigService.getById(configId);
        //ConfigDto conditionDto = new ConfigDto();
        //BeanUtil.copyProperties(config, conditionDto);
        return config;
    }

    /**
     * 查询列表
     */
    @ApiOperation("机器人配置列表")
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {

        //根据条件查询
        Page<Map<String, Object>> result = robotConfigService.selectByCondition(condition);
        Page wrapped = new ConfigWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }



    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "configId", dict = ConfigMap.class)
    @ResponseBody
    public ResponseData edit(RobotConfig config) {
        //if (StringUtils.isBlank(config.getValue())){
        //    return ResponseData.error("值不能为空");
        //}
        if (config == null) {
            return ResponseData.error("参数不能为空");
        }

        RobotConfig entity = robotConfigService.getById(config.getRobotId());


        //RobotConfig one1;
        //one1 = robotConfigService.getOne(new QueryWrapper<>(config));
        //if(one1!=null){
        //    throw new ServiceException(BizExceptionEnum.EXIST);
        //}

        //校验机器人账号
        Member query = new Member();
        query.setAccount(config.getAccount().trim()).setDel("N");
        Member one = memberService.getOne(new QueryWrapper<>(query));
        if (one==null){
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        entity.setRobotUserId(one.getMemberId());
        //校验撮合对象
        Member query2 = new Member();
        query2.setAccount(config.getAccount().trim()).setDel("N");
        Member one2 = memberService.getOne(new QueryWrapper<>(query));
        if (one2==null){
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        //公共参数
        entity.setUserId(one2.getMemberId());
        //config.setStatus("0");
        entity.setUpdateTime(new Date());
        robotConfigService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加", key = "name")
    @ResponseBody
    public ResponseData add(RobotConfig config, BindingResult result) {

        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (config == null) {
            return ResponseData.error("参数不能为空");
        }

        RobotConfig config1;
        config1 = robotConfigService.getOne(new QueryWrapper<>(config));
        if(config1!=null){
            throw new ServiceException(BizExceptionEnum.EXIST);
        }

        //校验机器人账号
        Member query = new Member();
        query.setAccount(config.getRobotAccount().trim()).setDel("N");
        Member jqr = memberService.getOne(new QueryWrapper<>(query));
        if (jqr==null){
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        config.setRobotUserId(jqr.getMemberId());
        //校验撮合对象
        if(config.getAccount().trim().equals("-1")){ //全站撮合
            RobotConfig robotConfig = new RobotConfig();
            robotConfig.setUserId(-1L);
            config1 = robotConfigService.getOne(new QueryWrapper<>(robotConfig));
            if(config1!=null){
                throw new ServiceException(BizExceptionEnum.EXIST);
            }
            //公共参数
            config.setUserId(-1L);
        }else{
            Member query2 = new Member();
            query2.setAccount(config.getAccount().trim()).setDel("N");
            Member one2 = memberService.getOne(new QueryWrapper<>(query2));
            if (one2==null){
                throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
            }
            //公共参数
            config.setUserId(one2.getMemberId());
        }
        config.setStatus("0");
        config.setCreateTime(new Date());
        this.robotConfigService.addRobotConfig(config);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除配置", key = "configId", dict = ConfigMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long configId) {
        if (ToolUtil.isEmpty(configId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.robotConfigService.removeById(configId);
        return SUCCESS_TIP;
    }

    /**
     * 开启关闭
     */
    @RequestMapping("/upStatus")
    @BussinessLog(value = "开启关闭", key = "robotId")
    @ResponseBody
    public ResponseData upStatus(@RequestParam Long robotId) {
        if (ToolUtil.isEmpty(robotId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        RobotConfig byId = robotConfigService.getById(robotId);
        if(byId.getStatus().equals("0")){
            byId.setStatus("1");
        }else {
            byId.setStatus("0");
        }
        byId.setUpdateTime(new Date());
        this.robotConfigService.updateById(byId);
        return SUCCESS_TIP;
    }
}
