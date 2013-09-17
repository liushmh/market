package org.jk.finance;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jk.finance.model.RealtimeInfo;
import org.jk.finance.model.StandardHistoricInfo;
import org.jk.finance.model.Stock;

public abstract class MarketServer {

	public abstract List<Stock> getWatchedStockList(String uid);
	
	/**
	 * Get real time stock info by stock name list.
	 * @param codeList
	 * @return a bunch of stock real time trans information.
	 */
	public abstract Map<String, RealtimeInfo> getRealtimeInfo(String[] codeList);
	
	
	public abstract List<StandardHistoricInfo> getHistoricInfo(String code, String start, String end);
}
