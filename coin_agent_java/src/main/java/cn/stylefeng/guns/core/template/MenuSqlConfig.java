package cn.stylefeng.guns.core.template;


import lombok.Data;

/**
 * 菜单sql脚本 模板 控制器
 */
@Data
public class MenuSqlConfig {

    private ContextConfig contextConfig;

    private String menuSqlPathTemplate;

    public void init() {
        menuSqlPathTemplate = "\\sql\\menu\\" + contextConfig.getTopMenuName() + "\\{}.sql";
    }

}
