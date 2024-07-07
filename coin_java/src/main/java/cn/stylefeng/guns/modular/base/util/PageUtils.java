package cn.stylefeng.guns.modular.base.util;

import java.io.Serializable;
import java.util.List;

public class PageUtils implements Serializable {

	private static final long serialVersionUID = 2695501996530722998L;

	private  int total;
	
	private List<?> rows;
	
	

	public PageUtils() {
		super();
	}

	public PageUtils(int total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "PageUtils [total=" + total + ", rows=" + rows + "]";
	}
	
	
}
