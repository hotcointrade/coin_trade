package cn.stylefeng.guns.modular.promotion.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HtmlUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.promotion.constant.ProblemMap;
import cn.stylefeng.guns.modular.promotion.entity.Problem;
import cn.stylefeng.guns.modular.promotion.service.ProblemService;
import cn.stylefeng.guns.modular.promotion.wrapper.ProblemWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 常见问题控制器
 *
 * @author yaying.liu
 * @Date 2019-10-14 14:11:50
 */
@Controller
@RequestMapping("/problem")
public class ProblemController extends BaseController {

    private String PREFIX = "/modular/promotion/problem/";

     @Autowired
     private ProblemService problemService;

    /**
     * 跳转到常见问题首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "problem.html";
    }

    /**
     * 跳转到添加常见问题
     */
    @RequestMapping("/problem_add")
    public String problemAdd() {
        return PREFIX + "problem_add.html";
    }

    /**
     * 跳转到修改常见问题
     */
    @RequestMapping("/problem_edit")
    public String problemEdit(Long problemId, Model model) {
        Problem condition = this.problemService.getById(problemId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "problem_edit.html";
    }

    /**
     * 常见问题详情
     */
    @RequestMapping(value = "/detail/{problemId}")
    @ResponseBody
    public Object detail(@PathVariable("problemId") Long problemId) {
        Problem entity = problemService.getById(problemId);
      //  ProblemDto conditionDto = new ProblemDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询常见问题列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = problemService.selectByCondition(condition);
        Page wrapped = new ProblemWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑常见问题
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(Problem dto) {
        Problem entity = problemService.getById(dto.getProblemId());
        entity.setTitle(dto.getTitle()).setRemark(dto.getRemark());
        //对传入数据进行反转义处理
        String tempContent = dto.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        entity.setContent(tempContent);
        entity.setLanguage(dto.getLanguage());
        problemService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加常见问题
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加常见问题", key = "name", dict = ProblemMap.class)
    @ResponseBody
    public ResponseData add(Problem problem, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (problem == null) {
            return ResponseData.error("参数不能为空");
        }
        //对传入数据进行反转义处理
        String tempContent = problem.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        problem.setContent(tempContent);


        this.problemService.addProblem(problem);
        return SUCCESS_TIP;
    }

    /**
     * 删除常见问题
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除常见问题", key = "problemId", dict = ProblemMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long problemId) {
        if (ToolUtil.isEmpty(problemId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.problemService.deleteProblem(problemId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{problemId}")
    @ResponseBody
    public Object content(@PathVariable("problemId") Long id) {
        Problem problem = problemService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(problem);
        return super.warpObject(new ProblemWrapper(stringObjectMap));
    }
}
