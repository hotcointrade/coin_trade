package cn.stylefeng.guns.core.template;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 字典
 * @author: yaying.liu
 * @date: 2019-08-10 16:44
 **/
@Data
public class MapConfig {

    private ContextConfig contextConfig;

    private String mapPathTemplate;

    private String packageName;

    private List<String> mapImports;

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap");
        this.mapImports = imports;
        this.mapPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\constant\\{}Map.java";
        this.packageName = "cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".constant";
    }

}
