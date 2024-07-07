package cn.stylefeng.guns.modular.fin.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionCoin;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
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
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.guns.modular.fin.wrapper.WalletWrapper;
import cn.stylefeng.guns.modular.fin.constant.WalletMap;

/**
 * 账户信息控制器
 *
 * @author yaying.liu
 * @Date 2020-03-12 17:05:14
 */
@Controller
@RequestMapping("/wallet")
public class WalletController extends BaseController {

    private String PREFIX = "/modular/fin/wallet/";

     @Autowired
     private WalletService walletService;

    /**
     * 跳转到账户信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "wallet.html";
    }

    /**
     * 跳转到添加账户信息
     */
    @RequestMapping("/wallet_add")
    public String walletAdd() {
        return PREFIX + "wallet_add.html";
    }

    /**
     * 跳转到修改账户信息
     */
    @RequestMapping("/wallet_edit")
    public String walletEdit(Long walletId, Model model) {
        Wallet condition = this.walletService.getById(walletId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "wallet_edit.html";
    }

    /**
     * 账户信息详情
     */
    @RequestMapping(value = "/detail/{walletId}")
    @ResponseBody
    public Object detail(@PathVariable("walletId") Long walletId) {
        Wallet entity = walletService.getById(walletId);
      //  WalletDto conditionDto = new WalletDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询账户信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition,@RequestParam(required = false)Double minPrice,@RequestParam(required = false)Double maxPrice) {
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = walletService.selectByCondition(minPrice,maxPrice,condition,memberId,recommendIds);
        Page wrapped = new WalletWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 查询现货交易对列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/wallet-list")
    @ResponseBody
    public ResponseData coinlist() {

        List<Wallet> list = this.walletService.list();
        return ResponseData.success(list);
    }
    /**
     * 编辑用户钱包
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "walletId", dict = WalletMap.class)
    @ResponseBody
    public ResponseData edit(Wallet wallet) {

        return walletService.edit(wallet);
    }


    /**
     * 添加账户信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加账户信息", key = "name", dict = WalletMap.class)
    @ResponseBody
    public ResponseData add(Wallet wallet, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (wallet == null) {
            return ResponseData.error("参数不能为空");
        }
        this.walletService.addWallet(wallet);
        return SUCCESS_TIP;
    }

    /**
     * 释放FOT
     */
    @RequestMapping("/releaseFot")
    @ResponseBody
    public ResponseData releaseFot() {
        this.walletService.releaseFot();
        return SUCCESS_TIP;
    }

    /**
     * 删除账户信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除账户信息", key = "walletId", dict = WalletMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long walletId) {
        if (ToolUtil.isEmpty(walletId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.walletService.deleteWallet(walletId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{walletId}")
    @ResponseBody
    public Object content(@PathVariable("walletId") Long id) {
        Wallet wallet = walletService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(wallet);
        return super.warpObject(new WalletWrapper(stringObjectMap));
    }
}
