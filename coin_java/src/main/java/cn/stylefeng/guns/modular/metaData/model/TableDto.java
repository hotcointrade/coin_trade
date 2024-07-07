package cn.stylefeng.guns.modular.metaData.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 表dto
 * @author: yaying.liu
 * @date: 2019-09-22 17:15
 **/
@Data
@Accessors(chain = true)
public class TableDto {

    /**
     * TABLE_NAME as '表名',
     * column_name AS '列名',
     * data_type AS '数据类型',
     * character_maximum_length AS '字符长度',
     * numeric_precision AS '数字长度',
     * numeric_scale AS '小数位数',
     * is_nullable AS '是否允许非空',
     * CASE WHEN extra = 'auto_increment'
     * THEN 1 ELSE 0 END AS '是否自增',
     * column_default AS '默认值',
     * column_comment AS '备注'
     */
    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 字符长度
     */
    private Integer characterMaximumLength;
    /**
     * 数字长度
     */
    private Integer numericPrecision;
    /**
     * 小数位数
     */
    private Integer numericScale;
    /**
     * 是否允许非空
     */
    private String isNullable;
    /**
     * 是否自增
     */
    private Integer isAutoIncrement;

    /**
     * 默认值
     */
    private String columnDefault;
    /**
     * 备注
     */
    private String columnComment;

    /**
     * 字段key
     */
    private String columnKey;

    /**
     * 数据源
     */
    private String tableSchema;

    /**
     * java数据类型
     */
    private String propertyType;

    /**
     * java 驼峰属性名
     */
    private String propertyName;

}
