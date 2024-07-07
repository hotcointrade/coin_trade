package cn.stylefeng.guns.core.template;

import cn.stylefeng.guns.modular.metaData.model.TableDto;
import lombok.Data;

import java.util.List;

/**
 * 页面 模板生成的配置
 *
 * @author yaying.liu
 * @date 2019-08-10
 */
@Data
public class PageConfig {

    private ContextConfig contextConfig;

    private String pagePathTemplate;
    private String pageAddPathTemplate;
    private String pageEditPathTemplate;
    private String pageJsPathTemplate;
    private String pageInfoJsPathTemplate;
    private String pageAddJsPathTemplate;
    private String pageEditJsPathTemplate;
    private List<TableDto> tableDtoList;

    public void init() {
        this.tableDtoList=contextConfig.getTableFieldList();
        pagePathTemplate = "\\src\\main\\webapp\\pages\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}.html";
        pageAddPathTemplate = "\\src\\main\\webapp\\pages\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}_add.html";
        pageEditPathTemplate = "\\src\\main\\webapp\\pages\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}_edit.html";
        pageJsPathTemplate = "\\src\\main\\webapp\\assets\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}.js";
        pageInfoJsPathTemplate = "\\src\\main\\webapp\\assets\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}_info.js";
        pageAddJsPathTemplate = "\\src\\main\\webapp\\assets\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}_add.js";
        pageEditJsPathTemplate = "\\src\\main\\webapp\\assets\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}_edit.js";

    }

}