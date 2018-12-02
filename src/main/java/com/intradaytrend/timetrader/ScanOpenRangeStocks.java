package com.intradaytrend.timetrader;

import com.zerodhatech.models.HistoricalData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        createAndSendReport(trendingDownStocks, "UP");

    }

    private static void createAndSendReport(List<String> trendingStocks, String trend) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("breakout.txt"))) {
            StringBuilder content = new StringBuilder();
            content.append("<b> " + trend + "Down Trending Stocks</b><br/>");
            trendingStocks.parallelStream().forEach((s -> {
                try {
                    content.append("<a href='https://www.google.com/search?tbm=fin&q=NSE:+" + s + "'>" + s + "</>" + "<br/>");
                    writer.write(s + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            SendReports.sendMail(content.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Get historical data for an instrument.
     */
    public static void getOpenRangeBreak(IntradayDataHolder intradayDataHolder, List<String> inTrendingDownStocks, List<String> inTrendingUpStocks) {
        HistoricalData historicalData = intradayDataHolder.getHistoricalData();
        if (!historicalData.dataArrayList.isEmpty()) {
            double high = historicalData.dataArrayList.get(14).high;
            double low = historicalData.dataArrayList.get(14).low;
            List<HistoricalData> historicalDataRange = historicalData.dataArrayList.subList(14, 29);
            for (HistoricalData candle : historicalDataRange) {
                if (candle.high > high) {
                    high = candle.high;
                }

                if (candle.low < low) {
                    low = candle.low;
                }
            }
            List<HistoricalData> historicalDataAfterRange = historicalData.dataArrayList.subList(30, historicalData.dataArrayList.size() - 1);
            double high2 = 0;
            double low2 = 0;
            for (HistoricalData candle : historicalDataAfterRange) {
                if (candle.high > high2) {
                    high2 = candle.high;
                }
                if (candle.low < low2) {
                    low2 = candle.low;
                }
            }
            if (high > high2) {
                inTrendingDownStocks.add(intradayDataHolder.getInstrumentCode());
            }
            if (low < low2) {
                inTrendingUpStocks.add(intradayDataHolder.getInstrumentCode());
            }
        }
    }
}


