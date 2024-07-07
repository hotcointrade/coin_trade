package cn.stylefeng.guns.modular.bulletin.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import java.math.BigDecimal;

/**
 * @Description:
 */
@Data
public class ExcelModel {

    @Excel(name = "检测类型")
    private String projectType;

    @Excel(name = "所属类型编码")
    private String typeCode;

    @Excel(name = "所属类型名称")
    private String typeValue;



    @Excel(name = "项目名")
    private String value;

    @Excel(name = "单价")
    private BigDecimal unit;

    @Excel(name = "备注")
    private String remark;


}