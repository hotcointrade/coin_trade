package cn.stylefeng.guns.modular.system.model;

import java.io.Serializable;

/**
 * 字典信息
 *
 */
public class DictDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型id
     */
    private Long dictTypeId;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 备注
     */
    private String description;
    /**
     * 序号
     */
    private Integer sort;
	public Long getDictTypeId() {
		return dictTypeId;
	}
	public void setDictTypeId(Long dictTypeId) {
		this.dictTypeId = dictTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    
    
}
