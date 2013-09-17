package org.jk.finance.model;

import org.joda.time.DateTime;



public class RealtimeInfo {
	private DateTime time;
	
	private float open;
	private float high;
	private float low;
	private float now; // current price
	
	private long vol;
	private float sumMoney;
	
	private long runningMinutes;

	public DateTime getTime() {
		return time;
		
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public float getNow() {
		return now;
	}

	public void setNow(float now) {
		this.now = now;
	}

	public long getVol() {
		return vol;
	}

	public void setVol(long vol) {
		this.vol = vol;
	}

	public float getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(float sumMoney) {
		this.sumMoney = sumMoney;
	}

	public long getRunningMinutes() {
		return runningMinutes;
	}

	public void setRunningMinutes(long runningMinutes) {
		this.runningMinutes = runningMinutes;
	}

	
}
