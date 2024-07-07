package cn.stylefeng.guns.modular.coin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.guns.modular.meta_data.constant.ConfigMap;
import cn.stylefeng.guns.modular.meta_data.entity.Config;
import cn.stylefeng.guns.modular.meta_data.model.ConfigDto;
import cn.stylefeng.guns.modular.meta_data.service.ConfigService;
import cn.stylefeng.guns.modular.meta_data.wrapper.ConfigWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
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
import java.util.List;
import java.util.Map;

/**
 * 配置参数控制器
 */
@Controller
@RequestMapping("/contract_option_config")
public class ContractOptionConfigController extends BaseController {

    private static String PREFIX = "/modular/coin/option/";

    @Autowired
    private ConfigService configService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LegalService legalService;
    @Autowired
    private MemberService memberService;


    /**
     * 跳转到管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "config.html";
    }


    /**
     * 跳转到添加
     */
    @RequestMapping("/config_add")
    public String configAdd() {
        return PREFIX + "config_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/config_edit")
    public String configEdit(Long configId, Model model) {
        Config condition = this.configService.getById(configId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX+"config_edit.html";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{configId}")
    @ResponseBody
    public Object detail(@PathVariable("configId") Long configId) {
        Config config = configService.getById(configId);
        ConfigDto conditionDto = new ConfigDto();
        BeanUtil.copyProperties(config, conditionDto);
        return conditionDto;
    }

    /**
     * 查询列表
     */
    @ApiOperation("配置列表")
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = configService.selectByConditionContractOption(condition);
        Page wrapped = new ConfigWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }



    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "configId", dict = ConfigMap.class)
    @ResponseBody
    public ResponseData edit(Config config) {
        if (StringUtils.isBlank(config.getValue())){
            return ResponseData.error("值不能为空");
        }
        Config entity=configService.getById(config.getConfigId());
        if(Constant.OTC_COIN.equals(entity.getCode())){
            ThreadPoolUtil.execute(
                    ()->{
                        updateWalletAndLegal(config.getValue());
                    }
            );
        }
        entity.setValue(config.getValue());

        configService.updateById(entity);

        return SUCCESS_TIP;
    }
    private void updateWalletAndLegal(String coinString) {
        String[] coins = coinString.split(",");
        for (String coin : coins) {
            Member entity = new Member();
            entity.setDel("N");
            List<Member> memberList = memberService.list(new QueryWrapper<>(entity));
            for (Member m :memberList){
                Legal wallet = F.me().getLegal(m.getMemberId(),coin);
                if (wallet == null){
                    wallet = new Legal();
                    wallet.setMemberId(m.getMemberId());
                    wallet.setType(coin);
                    wallet.setUsedPrice(BigDecimal.ZERO);
                    wallet.setLockedPrice(BigDecimal.ZERO);
                    wallet.setCreateUser(m.getMemberId());
                    wallet.setCreateTime(new Date());
                    wallet.setUpdateUser(m.getMemberId());
                    legalService.save(wallet);
                }

            }
        }
    }


    /**
     * 添加参数
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加参数", key = "name", dict = ConfigMap.class)
    @ResponseBody
    public ResponseData add(Config config, BindingResult result) {

        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (config == null) {
            return ResponseData.error("参数不能为空");
        }
        //公共参数
        this.configService.addConfigCommon(config);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除参数", key = "configId", dict = ConfigMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long configId) {
        if (ToolUtil.isEmpty(configId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.configService.deleteConfig(configId);
        return SUCCESS_TIP;
    }

}
