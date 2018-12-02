package com.intradaytrend.timetrader;

import com.zerodhatech.models.HistoricalData;

public class IntradayDataHolder {
    private String instrumentCode;
    private HistoricalData historicalData;

    public IntradayDataHolder(String instrumentCode, HistoricalData historicalData) {
        this.instrumentCode = instrumentCode;
        this.historicalData = historicalData;
    }
    public String getInstrumentCode() {
        return instrumentCode;
    }

    public HistoricalData getHistoricalData() {
        return historicalData;
    }



}
