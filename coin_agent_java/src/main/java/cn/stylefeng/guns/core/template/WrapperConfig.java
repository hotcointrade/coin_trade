package cn.stylefeng.guns.core.template;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yaying.liu
 * @date: 2019-08-10 16:27
 **/
@Data
public class WrapperConfig {

    private ContextConfig contextConfig;

    private String wrapperPathTemplate;

    private String packageName;

    private List<String> wrapperImports;

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper");
        imports.add("cn.stylefeng.roses.kernel.model.page.PageResult");
        imports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
        imports.add("java.util.List");
        imports.add("java.util.Map");
        this.wrapperImports = imports;
        this.wrapperPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\wrapper\\{}Wrapper.java";
        this.packageName = "cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".wrapper";
    }


}
