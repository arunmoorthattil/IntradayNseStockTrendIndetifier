package com.intradaytrend.timetrader;

import com.zerodhatech.models.HistoricalData;

import java.util.ArrayList;
import java.util.List;

public class ScanOpenRangeStocks {

    static void scanBreakDownStocks() {
        List<String> trendingDownStocks = new ArrayList<>();
        List<String> trendingUpStocks = new ArrayList<>();

        while (true) {
            IntradayDataHolder shareHolder = null;
            try {
                shareHolder = HistoricalDataReader.getDataHolder().take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shareHolder.getInstrumentCode().equals("DONE")) {
                break;
            }
            getOpenRangeBreak(shareHolder, trendingDownStocks, trendingUpStocks);
        }
        createAndSendReport(trendingDownStocks, "DOWN");
        createAndSendReport(trendingUpStocks, "UP");
    }

    private static void createAndSendReport(List<String> trendingStocks, String trend) {
        StringBuilder content = new StringBuilder();
        content.append("<b> " + trendingStocks.size() + "\t" + trend + " Trending Stocks</b><br/>");
        trendingStocks.parallelStream().forEach((s -> {
            content.append("<a href='https://www.google.com/search?tbm=fin&q=NSE:+").append(s).append("'>").append(s).append("</>").append("<br/>");

        }));
        SendReports.sendMail(content.toString());
    }


    /**
     * Get historical data for an instrument.
     */
    public static void getOpenRangeBreak(IntradayDataHolder intradayDataHolder, List<String> inTrendingDownStocks, List<String> inTrendingUpStocks) {
        HistoricalData historicalData = intradayDataHolder.getHistoricalData();
        if (!historicalData.dataArrayList.isEmpty()) {
            double high = historicalData.dataArrayList.get(15).high;
            double low = historicalData.dataArrayList.get(15).low;
            List<HistoricalData> historicalDataRange = historicalData.dataArrayList.subList(15, 30);
            for (HistoricalData candle : historicalDataRange) {
                if (candle.high > high) {
                    high = candle.high;
                }

                if (candle.low < low) {
                    low = candle.low;
                }
            }
            List<HistoricalData> historicalDataAfterRange = historicalData.dataArrayList.subList(31, 45);
            double high2 = 0;
            double low2 = historicalData.dataArrayList.get(31).low;
            for (HistoricalData candle : historicalDataAfterRange) {
                if (candle.high > high2) {
                    high2 = candle.high;
                }
                if (candle.low > low2) {
                    low2 = candle.low;
                }
            }
            boolean downTrend=false;
            if (high > high2) {
                inTrendingDownStocks.add(intradayDataHolder.getInstrumentCode());
                downTrend=true;
            }
            if (low < low2&&!downTrend) {
                inTrendingUpStocks.add(intradayDataHolder.getInstrumentCode());
            }
        }
    }
}


