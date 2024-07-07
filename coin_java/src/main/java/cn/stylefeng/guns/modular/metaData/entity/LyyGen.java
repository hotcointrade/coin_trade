package cn.stylefeng.guns.modular.metaData.entity;

import lombok.Data;

@Data
public class LyyGen {
    private String moduleName;
    private String bizChName;
    private String bizEnName;
    private String path;
    private String topMenuName ;  //上级菜单名称
    private String sort;//当前菜单排序号
    private String tableSchema;//数据源
    private String bizTableName;//映射表名称

}
