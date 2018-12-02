package com.intradaytrend.timetrader;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import static com.intradaytrend.timetrader.NseInstruments.TRADE_INSTRUMENTS;

public class HistoricalDataReader {
    public static LinkedBlockingQueue<IntradayDataHolder> getDataHolder() {
        return _dataHolder;
    }

    private static final LinkedBlockingQueue<IntradayDataHolder> _dataHolder = new LinkedBlockingQueue<>();

    public static void intializeDataForScan(KiteConnect inKiteConnect) {
        Set<String> instruments = TRADE_INSTRUMENTS.keySet();
        for (String instrument : instruments) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                final Date from = formatter.parse(LocalDate.now() + " 09:15:00");
                final Date to = formatter.parse(LocalDate.now() + " 10:00:00");
                HistoricalData historicalData = inKiteConnect.getHistoricalData(from, to, instrument, "minute", false);
                _dataHolder.add(new IntradayDataHolder(TRADE_INSTRUMENTS.get(instrument), historicalData));

                Thread.sleep(100);
            } catch (KiteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        _dataHolder.add(new IntradayDataHolder("DONE",null));
    }
}
