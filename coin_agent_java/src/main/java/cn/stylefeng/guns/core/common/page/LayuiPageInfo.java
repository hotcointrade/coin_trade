package cn.stylefeng.guns.core.common.page;

import java.util.List;

/**
 * 分页结果的封装(for Layui Table)
 */
public class LayuiPageInfo {

    private Integer code = 0;

    private String msg = "请求成功";

    @SuppressWarnings("rawtypes")
	private List data;

    private long count;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@SuppressWarnings("rawtypes")
	public List getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List data) {
		this.data = data;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
    
    

}
