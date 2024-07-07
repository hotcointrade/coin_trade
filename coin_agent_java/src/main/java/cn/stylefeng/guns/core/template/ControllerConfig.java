package cn.stylefeng.guns.core.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制器模板生成的配置
 *
 */
public class ControllerConfig {

    private ContextConfig contextConfig;

    private String controllerPathTemplate;
    private String packageName;//包名称
    private List<String> imports;//所引入的包

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("cn.stylefeng.roses.core.base.controller.BaseController");
        imports.add("org.springframework.stereotype.Controller");
        imports.add("org.springframework.web.bind.annotation.RequestMapping");
        imports.add("org.springframework.web.bind.annotation.ResponseBody");
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.web.bind.annotation.RequestParam");
        imports.add("org.springframework.ui.Model");
        imports.add("org.springframework.web.bind.annotation.PathVariable");
        imports.add("org.springframework.validation.BindingResult");
        imports.add("cn.hutool.core.bean.BeanUtil");
        imports.add("cn.stylefeng.guns.core.common.page.LayuiPageFactory");
        imports.add("cn.stylefeng.guns.core.common.annotion.BussinessLog");
        imports.add("cn.stylefeng.guns.core.common.annotion.Permission");
        imports.add("cn.stylefeng.guns.core.common.constant.Const");
        imports.add("cn.stylefeng.guns.core.common.exception.BizExceptionEnum");
        imports.add("cn.stylefeng.guns.core.common.page.LayuiPageFactory");
        imports.add("cn.stylefeng.guns.core.log.LogObjectHolder");
        imports.add("cn.stylefeng.roses.core.util.ToolUtil");
        imports.add("cn.stylefeng.roses.kernel.model.exception.ServiceException");
        imports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
        imports.add("java.util.Map");
        imports.add("cn.stylefeng.roses.core.reqres.response.ResponseData");

        this.imports = imports;
        this.packageName = "cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".controller";
        this.controllerPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\controller\\{}Controller.java";
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getControllerPathTemplate() {
        return controllerPathTemplate;
    }

    public void setControllerPathTemplate(String controllerPathTemplate) {
        this.controllerPathTemplate = controllerPathTemplate;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }
}