package com.intradaytrend.timetrader;

import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Component
public class RunIntradayTimeScanner {
    public void runScan(KiteConnect kiteConnect){
        ExecutorService executor = Executors.newFixedThreadPool(2
        );
        executor.submit(()->{HistoricalDataReader.intializeDataForScan(kiteConnect);});
        executor.submit(ScanOpenRangeStocks::scanBreakDownStocks);
        executor.shutdown();
    }
}
