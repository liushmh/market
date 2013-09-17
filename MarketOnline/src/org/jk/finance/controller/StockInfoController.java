package org.jk.finance.controller;

import java.util.List;
import java.util.Map;

import org.jk.finance.MainlandMarketServer;
import org.jk.finance.MarketServer;
import org.jk.finance.model.RealtimeInfo;
import org.jk.finance.model.Stock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StockInfoController {

	@RequestMapping("/gw")
	public @ResponseBody
	List<Stock> getStockListInJSON() {

		MarketServer ms = new MainlandMarketServer();
		return ms.getWatchedStockList("");
	}

	@RequestMapping("/rt")
	public @ResponseBody
	Map<String, RealtimeInfo> getStockRealtimeInJSON(
			@RequestParam("cl") String cl) {
		String[] codeList = cl.split(",");
		MarketServer ms = new MainlandMarketServer();
		return ms.getRealtimeInfo(codeList);
	}

}
