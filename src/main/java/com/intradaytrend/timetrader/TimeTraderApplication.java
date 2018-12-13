package com.intradaytrend.timetrader;

import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TimeTraderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(TimeTraderApplication.class, args);
        KiteLogin kiteLogin = appContext.getBean(KiteLogin.class);
        kiteLogin.login();
        KiteConnect kiteConnect =kiteLogin.getKiteConnect();
        assert kiteConnect!=null;
        RunIntradayTimeScanner scanner = appContext.getBean(RunIntradayTimeScanner.class);
        scanner.runScan(kiteConnect);
    }
}
