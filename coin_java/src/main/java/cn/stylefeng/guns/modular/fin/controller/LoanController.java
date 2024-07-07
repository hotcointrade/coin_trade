package cn.stylefeng.guns.modular.fin.controller;

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
import cn.stylefeng.guns.modular.fin.service.LoanService;
import cn.stylefeng.guns.modular.fin.wrapper.LoanWrapper;
import cn.stylefeng.guns.modular.fin.entity.Loan;
import cn.stylefeng.guns.modular.fin.constant.LoanMap;

/**
 * 申请控制器
 *
 * @author yaying.liu
 * @since 2022-10-18 20:42:53
 */
@Controller
@RequestMapping("/loan")
public class LoanController extends BaseController {

    private String PREFIX = "/modular/fin/loan/";

     @Autowired
     private LoanService loanService;

    /**
     * 跳转到申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "loan.html";
    }

    /**
     * 跳转到添加申请
     */
    @RequestMapping("/loan_add")
    public String loanAdd() {
        return PREFIX + "loan_add.html";
    }

    /**
     * 跳转到修改申请
     */
    @RequestMapping("/loan_edit")
    public String loanEdit(Long loanId, Model model) {
        Loan condition = this.loanService.getById(loanId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "loan_edit.html";
    }
    /**
     * 跳转到审核申请
     */
    @RequestMapping("/loan_pass")
    public String loanPass(Long loanId, Model model) {
        Loan condition = this.loanService.getById(loanId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "loan_pass.html";
    }

    /**
     * 申请详情
     */
    @RequestMapping(value = "/detail/{loanId}")
    @ResponseBody
    public Object detail(@PathVariable("loanId") Long loanId) {
        Loan entity = loanService.getById(loanId);
      //  LoanDto conditionDto = new LoanDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }
    /**
     * 申请详情
     */
    @RequestMapping(value = "/pass/{loanId}")
    @ResponseBody
    public ResponseData pass(@PathVariable("loanId") Long loanId) {
        Loan loan = loanService.getById(loanId);
        loan.setStatus("PASS");
        loanService.updateById(loan);
        return SUCCESS_TIP;
    }
    /**
     * 申请详情
     */
    @RequestMapping(value = "/noPass/{loanId}")
    @ResponseBody
    public ResponseData noPass(@PathVariable("loanId") Long loanId) {
        Loan loan = loanService.getById(loanId);
        loan.setStatus("NoPass");
        loanService.updateById(loan);
        return SUCCESS_TIP;
    }



    /**
     * 查询申请列表
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
        Page<Map<String, Object>> result = loanService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new LoanWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑申请
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "loanId", dict = LoanMap.class)
    @ResponseBody
    public ResponseData edit(Loan loan) {
        loanService.updateById(loan);
        return SUCCESS_TIP;
    }

    /**
     * 添加申请
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加申请", key = "name", dict = LoanMap.class)
    @ResponseBody
    public ResponseData add(Loan loan, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (loan == null) {
            return ResponseData.error("参数不能为空");
        }
        this.loanService.addLoan(loan);
        return SUCCESS_TIP;
    }

    /**
     * 删除申请
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除申请", key = "loanId", dict = LoanMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long loanId) {
        if (ToolUtil.isEmpty(loanId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.loanService.deleteLoan(loanId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{loanId}")
    @ResponseBody
    public Object content(@PathVariable("loanId") Long id) {
        Loan loan = loanService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(loan);
        return super.warpObject(new LoanWrapper(stringObjectMap));
    }
}
