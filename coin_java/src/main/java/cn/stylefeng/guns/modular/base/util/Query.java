package cn.stylefeng.guns.modular.base.util;


import java.util.LinkedHashMap;
import java.util.Map;

public class Query extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -8189205736185009727L;
	
	private int pageNumber ;
	
	private int pageSize;

	public Query(Map<String,Object> params) {
		this.putAll(params);
		if(params.get("pageSize") != null && params.get("pageNumber") != null) {
			this.pageSize = Integer.parseInt(params.get("pageSize").toString() );
			this.pageNumber = Integer.parseInt(params.get("pageNumber").toString() );
			this.put("offset", (pageNumber-1)*pageSize);
			this.put("limit", pageSize);
		}
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	

}
