package util;

public class ValidateException extends Exception{
	public String errmsg;
	public ValidateException(String msg)
	{
		this.errmsg=msg;
	}

}
