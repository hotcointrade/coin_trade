package cn.stylefeng.guns.modular.bulletin.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 检测项目- 导入模板
 */
@Data
public class ExcelProject {

    @Excel(name = "项目名")
    private String value;

    @Excel(name = "单价")
    private BigDecimal unit;

    @Excel(name = "备注")
    private String remark;
}
