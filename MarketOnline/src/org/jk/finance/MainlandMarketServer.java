package org.jk.finance;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.jk.finance.model.RealtimeInfo;
import org.jk.finance.model.StandardHistoricInfo;
import org.jk.finance.model.Stock;
import org.jk.finance.type.ServerType;
import org.jk.finance.util.GetDataFromYahooUtil;
import org.jk.finance.util.GetStockNameUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class MainlandMarketServer extends MarketServer {

	public MainlandMarketServer() {

	}

	private final int millisecondsInMinutes = 60 * 1000;

	private String pattern = "hh:mm:ss";
	private String openTime = "9:30:00";
	private String secondOpenTime = "13:00:00";

	private String url4RealTime = "http://hq.sinajs.cn/?list=";
	private String url4HistoricInfo;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		openTime = openTime;
	}

	public String getSecondOpenTime() {
		return secondOpenTime;
	}

	public void setSecondOpenTime(String secondOpenTime) {
		this.secondOpenTime = secondOpenTime;
	}

	public String getUrl4RealTime() {
		return url4RealTime;
	}

	public void setUrl4RealTime(String url4RealTime) {
		this.url4RealTime = url4RealTime;
	}

	public String getUrl4HistoricInfo() {
		return url4HistoricInfo;
	}

	public void setUrl4HistoricInfo(String url4HistoricInfo) {
		this.url4HistoricInfo = url4HistoricInfo;
	}

	@Override
	public List<Stock> getWatchedStockList(String uid) {
		// TODO Auto-generated method stub
		List<Stock> retList = new ArrayList<Stock>();
		retList.add(new Stock("600880"));
		return retList;
	}

	@Override
	public Map<String, RealtimeInfo> getRealtimeInfo(String[] codeList) {
		// TODO Auto-generated method stub
		List<String> cl = new ArrayList<String>();

		for (String code : codeList) {
			cl.add(GetStockNameUtil.getCodeName(code, ServerType.sina));
		}
		Map<String, RealtimeInfo> retMap = new HashMap<String, RealtimeInfo>();
		List<RealtimeInfo> retList = new ArrayList<RealtimeInfo>();

		String s = StringUtils.join(cl, ",");

		URL url;
		try {
			url = new URL(this.url4RealTime + s);

			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(16000);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			// DateTime.parse("", DateTimeFormat.forPattern(pattern));

			if (sb.length() > 0) {
				String rt = sb.toString();
				String[] rtList = rt.split(";");
				
				for (int i = 0; i < rtList.length; i++) { // see line 150, using traditional 'for loop' to add stock code easily 
					String rs = rtList[i];
					rs = rs.substring(rs.indexOf("\"") + 1,
							rs.lastIndexOf("\""));
					String[] rss = rs.split(",");
					// 1 open, 2 last day close, 3 now, 4 high, 5 low, 6 7
					// ignore, 8
					// vol, 9 sum money, length-2 time
					if (rss.length > 10) {
						RealtimeInfo ri = new RealtimeInfo();
						ri.setOpen(Float.parseFloat(rss[1]));
						ri.setNow(Float.parseFloat(rss[3]));
						ri.setHigh(Float.parseFloat(rss[4]));
						ri.setLow(Float.parseFloat(rss[5]));
						ri.setVol(Long.parseLong(rss[8]));
						ri.setSumMoney(Float.parseFloat(rss[9]));
						DateTime dt = new DateTime(
								new SimpleDateFormat(pattern)
										.parse(rss[rss.length - 2]));
						ri.setTime(dt);
						ri.setRunningMinutes(getRunningMinutes(dt));
						retMap.put(codeList[i], ri);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}

	@Override
	public List<StandardHistoricInfo> getHistoricInfo(String code,
			String start, String end) {
		// TODO Auto-generated method stub

		return GetDataFromYahooUtil.getStockCsvData(
				GetStockNameUtil.getCodeName(code, ServerType.yahoo), start,
				end);
	}

	private long getRunningMinutes(DateTime dt) {
		try {
			DateTime open = new DateTime(
					new SimpleDateFormat(this.getPattern()).parse(this
							.getOpenTime()));
			DateTime secondOpen = new DateTime(new SimpleDateFormat(
					this.getPattern()).parse(this.getSecondOpenTime()));

			long dtMillis = dt.getMillis();
			long minutes = 0;

			long openMillis = open.getMillis();
			long secondOpenMillis = secondOpen.getMillis();

			if (secondOpenMillis < dtMillis) {
				minutes = (dtMillis - secondOpenMillis)
						/ this.millisecondsInMinutes;
				if (minutes > 120) {
					minutes = 240;
				} else {
					minutes += 120;
				}
			} else if (openMillis < dtMillis) {
				minutes = (dtMillis - openMillis) / this.millisecondsInMinutes;
				if (minutes > 120) {
					minutes = 120;
				}
			}
			return minutes;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args) {
		GetDataFromYahooUtil stockUtil = new GetDataFromYahooUtil();
		MainlandMarketServer mms = new MainlandMarketServer();

		// List<StandardHistoricInfo> sdList = mms.getHistoricInfo("600880.ss",
		// "2013-9-16", "2013-9-16");
		// System.out.println(sdList.get(0).getHigh());
		// List<String> codeList = new ArrayList<String>();
		// String code = "sh600880";
		// codeList.add(code);
		// Map<String, RealtimeInfo> map = mms.getRealtimeInfo(codeList);
		// ObjectWriter ow = new ObjectMapper().writer()
		// .withDefaultPrettyPrinter();
		// String json;
		// try {
		// json = ow.writeValueAsString(map);
		// System.out.println(json);
		// } catch (JsonGenerationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
