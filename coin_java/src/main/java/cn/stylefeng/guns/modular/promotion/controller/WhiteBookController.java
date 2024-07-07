package cn.stylefeng.guns.modular.promotion.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HtmlUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.promotion.entity.WhiteBook;
import cn.stylefeng.guns.modular.promotion.service.WhiteBookService;
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


@Controller
@RequestMapping("/whiteBook")
public class WhiteBookController extends BaseController {

    private String PREFIX = "/modular/promotion/whiteBook/";

     @Autowired
     private WhiteBookService whiteBookService;

    /**
     * 跳转到常见问题首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "white_book.html";
    }

    /**
     * 跳转到添加常见问题
     */
    @RequestMapping("/white_book_add")
    public String problemAdd() {
        return PREFIX + "white_book_add.html";
    }

    /**
     * 跳转到修改常见问题
     */
    @RequestMapping("/white_book_edit")
    public String problemEdit(Long id, Model model) {
        WhiteBook condition = this.whiteBookService.getById(id);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "white_book_edit.html";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        WhiteBook entity = whiteBookService.getById(id);
        return entity;
    }


    /**
     * 查询
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = whiteBookService.selectByCondition(condition);
        Page wrapped = new ProblemWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(WhiteBook dto) {
        WhiteBook entity = whiteBookService.getById(dto.getId());
        entity.setLanguage(dto.getLanguage());
        entity.setTitle(dto.getTitle()).setRemark(dto.getRemark());
        //对传入数据进行反转义处理
        String tempContent = dto.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        entity.setContent(tempContent);
        whiteBookService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(WhiteBook problem, BindingResult result) {
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
        this.whiteBookService.addProblem(problem);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.whiteBookService.deleteProblem(id);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{id}")
    @ResponseBody
    public Object content(@PathVariable("id") Long id) {
        WhiteBook problem = whiteBookService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(problem);
        return super.warpObject(new ProblemWrapper(stringObjectMap));
    }
}
