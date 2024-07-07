package cn.stylefeng.guns.modular.promotion.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.promotion.entity.Information;
import cn.stylefeng.guns.modular.promotion.service.InformationService;
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
import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping("/information")
public class InformationController extends BaseController {

    private String PREFIX = "/modular/promotion/information/";

     @Autowired
     private InformationService informationService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "information.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/information_add")
    public String InformationAdd() {
        return PREFIX + "information_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/information_edit/{id}")
    public String informationCategoryEdit(@PathVariable("id")Long id, Model model) {
        Information condition = this.informationService.getById(id);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "information_edit.html";
    }



    /**
     * 查询列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = informationService.selectByCondition(condition);
        Page wrapped = new ProblemWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(Information dto) {
        Information entity = informationService.getById(dto.getId());
        entity.setTitle(dto.getTitle())
                .setRemark(dto.getRemark());
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(1L);
        entity.setCover(dto.getCover());
        entity.setContent(dto.getContent());
        entity.setCategoryId(dto.getCategoryId());
        informationService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(Information problem, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (problem == null) {
            return ResponseData.error("参数不能为空");
        }
        problem.setCreateTime(new Date());
        problem.setCreateUser(1L);
        problem.setDel("N");
        problem.setStatus("Y");
        this.informationService.save(problem);
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
        Information entity = informationService.getById(id);
        entity.setDel("Y");
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(1l);
        this.informationService.updateById(entity);
        return SUCCESS_TIP;
    }

}