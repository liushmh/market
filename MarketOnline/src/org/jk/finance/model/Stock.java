package org.jk.finance.model;

import java.util.Date;
import java.util.List;

public class Stock {
	
	public Stock(String code){
		this.code = code;
	}
	
	private String code;
	
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public List<StandardHistoricInfo> getHistoricInfo(Date start, Date end)
	{
		return null;
		
	}
	
	
	
}
