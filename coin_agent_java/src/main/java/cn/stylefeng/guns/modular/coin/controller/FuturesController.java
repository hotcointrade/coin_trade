package cn.stylefeng.guns.modular.coin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.coin.entity.Futures;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.coin.service.FuturesService;
import cn.stylefeng.guns.modular.coin.service.SwapService;
import cn.stylefeng.guns.modular.coin.wrapper.SwapWrapper;
import cn.stylefeng.guns.modular.fin.entity.Contract;
import cn.stylefeng.guns.modular.fin.service.ContractService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合约交易对控制器
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
@Controller
@RequestMapping("/futures")
public class FuturesController extends BaseController {

    private String PREFIX = "/modular/coin/futures/";

     @Autowired
     private FuturesService swapService;

     @Resource
     private RedisUtil redisUtil;

     @Resource
     private ContractService contractService;

     @Resource
     private MemberService memberService;

    /**
     * 跳转到合约交易对首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "swap.html";
    }

    /**
     * 跳转到添加合约交易对
     */
    @RequestMapping("/swap_add")
    public String swapAdd() {
        return PREFIX + "swap_add.html";
    }

    /**
     * 跳转到修改合约交易对
     */
    @RequestMapping("/swap_edit")
    public String swapEdit(Long swapId, Model model) {
        Futures condition = this.swapService.getById(swapId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "swap_edit.html";
    }

    /**
     * 合约交易对详情
     */
    @RequestMapping(value = "/detail/{swapId}")
    @ResponseBody
    public Object detail(@PathVariable("swapId") Long swapId) {
        Futures entity = swapService.getById(swapId);
      //  SwapDto conditionDto = new SwapDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询合约交易对列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String timeLimit) {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> result = swapService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new SwapWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑合约交易对
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "swapId", dict = SwapMap.class)
    @ResponseBody
    public ResponseData edit(Futures swap) {
        swapService.updateById(swap);
        return SUCCESS_TIP;
    }

    /**
     * 添加合约交易对
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加合约交易对", key = "name", dict = SwapMap.class)
    @ResponseBody
    public ResponseData add(Futures swap, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (swap == null) {
            return ResponseData.error("参数不能为空");
        }
        this.swapService.addSwap(swap);
        redisUtil.set("UPDATE_FUTURES","Y");
        ThreadPoolUtil.execute(
                ()->{
                    updateContract("add",swap.getSymbol());
                }
        );
        return SUCCESS_TIP;
    }

    /**
     * 删除合约交易对
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除合约交易对", key = "swapId", dict = SwapMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long swapId) {
        if (ToolUtil.isEmpty(swapId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.swapService.deleteSwap(swapId);
        redisUtil.set("UPDATE_SWAP","Y");
        ThreadPoolUtil.execute(
                ()->{
                    updateContract("delete",null);
                }
        );
        return SUCCESS_TIP;
    }
    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    @RequestMapping("/status/{status}")
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status,@RequestParam Long id) {
        Futures entity = swapService.getById(id);
        entity.setStatus(status);
        swapService.updateById(entity);
        redisUtil.set("UPDATE_FUTURES","Y");
        return SUCCESS_TIP;
    }
    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{swapId}")
    @ResponseBody
    public Object content(@PathVariable("swapId") Long id) {
        Futures swap = swapService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(swap);
        return super.warpObject(new SwapWrapper(stringObjectMap));
    }

    @RequestMapping(value = "/getList")
    @ResponseBody
    public ResponseData getList() {
        Futures swap = new Futures();
        swap.setDel("N");
        swap.setStatus("Y");
        List<Futures> list = 	swapService.list(new QueryWrapper<>(swap));
        return  ResponseData.success(list);
    }

    private void updateContract(String type,String coin) {
        Futures spot = new Futures();
        if ("delete".equals(type)) {
            List<Futures> swapList = F.me().getFuturess(null);
            for (Futures entity : swapList) {
                coin = entity.getSymbol().split("/")[0];
                Contract contract = new Contract();
                contract.setType(coin);
                contract.setDel("N");
                List<Contract> walletList = contractService.list(new QueryWrapper<>(contract));
                for (Contract wallet1 : walletList) {
                    wallet1.setDel("Y");
                    wallet1.setUpdateTime(new Date());
                    wallet1.setUpdateUser(1l);
                    contractService.updateById(wallet1);
                }
            }
        }else if ("add".equals(type) && StringUtils.isNotBlank(coin)){
            Member entity = new Member();
            entity.setDel("Y");
            List<Member> memberList = memberService.list(new QueryWrapper<>(entity));
            for (Member m :memberList){
                coin = coin.split("/")[0];
                Contract contract = F.me().getContract(m.getMemberId(),coin);
                if (contract == null){
                    contract = new Contract();
                    contract.setMemberId(m.getMemberId());
                    contract.setType(coin);
                    contract.setUsedPrice(BigDecimal.ZERO);
                    contract.setCreateUser(m.getMemberId());
                    contract.setUpdateUser(m.getMemberId());
                    contractService.save(contract);
                }
            }
        }
    }

}