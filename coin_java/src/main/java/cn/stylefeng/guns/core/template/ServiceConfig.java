package cn.stylefeng.guns.core.template;

import java.util.ArrayList;
import java.util.List;

/**
 * Service模板生成的配置
 *
 */
public class ServiceConfig {

    private ContextConfig contextConfig;

    private String servicePathTemplate;
    private String serviceImplPathTemplate;

    private String packageName;

    private List<String> serviceImplImports;

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("org.springframework.stereotype.Service");
        imports.add("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        imports.add("cn.stylefeng.guns.core.common.page.LayuiPageFactory");
        imports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
        imports.add("java.util.Map");
//        imports.add("com.stylefeng.guns.modular." + contextConfig.getModuleName() + ".service." + contextConfig.getBizEnBigName() + "Service");
        this.serviceImplImports = imports;
        this.servicePathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\service\\{}Service.java";
        this.serviceImplPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\service\\impl\\{}ServiceImpl.java";
        this.packageName = "cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".service";
    }


    public String getServicePathTemplate() {
        return servicePathTemplate;
    }

    public void setServicePathTemplate(String servicePathTemplate) {
        this.servicePathTemplate = servicePathTemplate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getServiceImplPathTemplate() {
        return serviceImplPathTemplate;
    }

    public void setServiceImplPathTemplate(String serviceImplPathTemplate) {
        this.serviceImplPathTemplate = serviceImplPathTemplate;
    }

    public List<String> getServiceImplImports() {
        return serviceImplImports;
    }

    public void setServiceImplImports(List<String> serviceImplImports) {
        this.serviceImplImports = serviceImplImports;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }
}

