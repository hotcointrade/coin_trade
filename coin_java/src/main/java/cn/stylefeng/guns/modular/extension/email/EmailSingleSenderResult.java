package cn.stylefeng.guns.modular.extension.email;

public class EmailSingleSenderResult {
/*
{
    "result": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
 */
	public int result;
	public String errmsg ;
	public Integer surplus;
	public String sequenceId;
	@Override
	public String toString() {
		return "EmailSingleSenderResult [result=" + result + ", errmsg=" + errmsg + ", surplus=" + surplus
				+ ", sequenceId=" + sequenceId + "]";
	}
	
	
}
