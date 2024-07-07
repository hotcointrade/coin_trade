package cn.stylefeng.guns.modular.app.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 流水类型 </p>
 */

@Data
public class FlowTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String type;

    private  String code;

    private String  sources;

    private String hkSources;

    private String hgSources;

    private String jpSources;

    private String usSource;

    private Date createTime;

    private Date updateTime;


}