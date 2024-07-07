package cn.stylefeng.guns.modular.base.util;

import cn.stylefeng.guns.modular.app.common.ApiStatus;

import java.io.Serializable;
import java.util.HashMap;

public class Result implements Serializable {

	private static final long serialVersionUID = 531702546012353243L;

	private int code = 200;

	private Object data;

	private String msg;

	private Boolean  success = true;

	public static Result  success(){
		Result  result = new Result();
		result.setSuccess(true);
		return result;
	}




//	public static Result  success(String message){
//		Result  result = new Result();
//		result.setSuccess(true);
//		result.setMsg(message);
//		return result;
//	}

	public static Result success(ApiStatus apiStatus)
	{
		Result  result = new Result();
		result.setSuccess(true);
		result.setMsg(apiStatus.msg());
		result.setCode(apiStatus.code());
		result.setData(new HashMap<>());
		return result;
	}

	public static Result success(ApiStatus apiStatus,String msg)
	{
		Result  result = new Result();
		result.setSuccess(true);
		result.setMsg(msg);
		result.setCode(apiStatus.code());
		result.setData(new HashMap<>());
		return result;

	}

	public static Result  success(String message,int code){
		Result  result = new Result();
		result.setSuccess(true);
		result.setCode(code);
		result.setMsg(message);
		result.setData(new HashMap<>());
		return result;
	}

	public static Result  success(String message,Object  data){
		Result  result = new Result();
		result.setSuccess(true);
		result.setData(data);
		result.setMsg(message);
		return result;
	}

	public static Result  success(Object  data){
		Result  result = new Result();
		result.setSuccess(true);
		result.setData(data);
		return result;
	}

	public static Result  success(String message,int code,Object data) {
		Result  result = new Result();
		result.setSuccess(true);
		result.setCode(code);
		result.setData(data);
		result.setMsg(message);
		return result;
	}

	public static Result  fail(int code){
		Result  result = new Result();
		result.setSuccess(false);
		result.setCode(code);
		result.setData(new HashMap<>());
		return result;
	}

	public static Result  fail(int code,String message){
		Result  result = new Result();
		result.setSuccess(false);
		result.setMsg(message);
		result.setCode(code);
		result.setData(new HashMap<>());
		return result;
	}


	public static Result fail(ApiStatus apiStatus){
		Result  result = new Result();
		result.setSuccess(false);
		result.setMsg(apiStatus.msg());
		result.setCode(apiStatus.code());
		result.setData(new HashMap<>());
		return result;
	}


	public static Result fail(ApiStatus apiStatus,String msg)
	{
		Result  result = new Result();
		result.setSuccess(false);
		result.setMsg(msg);
		result.setCode(apiStatus.code());
		result.setData(new HashMap<>());
		return result;
	}


	public Result() {

	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}


}
