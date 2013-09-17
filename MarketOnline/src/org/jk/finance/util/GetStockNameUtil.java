package org.jk.finance.util;

import org.jk.finance.type.ServerType;

public class GetStockNameUtil {

	public static String getCodeName(String code, ServerType type)
	{
		if (type == ServerType.yahoo){
			if (code.startsWith("6"))
				return  code + ".SS";
			else
				return code + ".SZ";
		}
		else if(type == ServerType.sina)
		{
			if (code.startsWith("6"))
				return "sh" + code;
			else
				return "sz" + code;
		}
		return "";
	}
}
