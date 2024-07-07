package cn.stylefeng.guns.modular.bulletin.model;

import lombok.Data;

import java.util.Date;

@Data
public class AppOperationLogDto {
    private Date createTime;
    private String message;
}
