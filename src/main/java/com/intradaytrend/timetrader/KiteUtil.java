package com.intradaytrend.timetrader;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 15/10/16.
 */
public class KiteUtil {


    public void getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
        Profile profile = kiteConnect.getProfile();
        System.out.println(profile.userName);
    }

    /**
     * Gets Margin.
     */
    public void getMargins(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
        //Margins margins = kiteConnect.getMargins("equity");
        Margin margins = kiteConnect.getMargins("equity");
        System.out.println(margins.available.cash);
        System.out.println(margins.utilised.debits);
        System.out.println(margins.utilised.m2mUnrealised);
    }

    /**
     * Place order.
     */
    public void placeOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Place order method requires a orderParams argument which contains,
         * tradingsymbol, exchange, transaction_type, order_type, quantity, product, price, trigger_price, disclosed_quantity, validity
         * squareoff_value, stoploss_value, trailing_stoploss
         * and variety (value can be regular, bo, co, amo)
         * place order will return order model which will have only orderId in the order model
         *
         * Following is an example param for LIMIT order,
         * if a call fails then KiteException will have error message in it
         * Success of this call implies only order has been placed successfully, not order execution. */

        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = "ASHOKLEY";
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 122.2;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "myTag"; //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

        Order order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        System.out.println(order.orderId);
    }

    /**
     * Place bracket order.
     */
    public void placeBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Bracket order:- following is example param for bracket order*
         * trailing_stoploss and stoploss_value are points and not tick or price
         */
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.price = 30.5;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.trailingStoploss = 1.0;
        orderParams.stoploss = 2.0;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.squareoff = 3.0;
        orderParams.product = Constants.PRODUCT_MIS;
        Order order10 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
        System.out.println(order10.orderId);
    }

    /**
     * Place cover order.
     */
    public void placeCoverOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Cover Order:- following is an example param for the cover order
         * key: quantity value: 1
         * key: price value: 0
         * key: transaction_type value: BUY
         * key: tradingsymbol value: HINDALCO
         * key: exchange value: NSE
         * key: validity value: DAY
         * key: trigger_price value: 157
         * key: order_type value: MARKET
         * key: variety value: co
         * key: product value: MIS
         */
        OrderParams orderParams = new OrderParams();
        orderParams.price = 0.0;
        orderParams.quantity = 1;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.triggerPrice = 30.5;
        orderParams.product = Constants.PRODUCT_MIS;

        Order order11 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_CO);
        System.out.println(order11.orderId);
    }

    /**
     * Get trigger range.
     */
    public void getTriggerRange(KiteConnect kiteConnect) throws KiteException, IOException {
        // You need to send transaction_type, exchange and tradingsymbol to get trigger range.
        String[] instruments = {"BSE:INFY", "NSE:APOLLOTYRE", "NSE:SBIN"};
        Map<String, TriggerRange> triggerRangeMap = kiteConnect.getTriggerRange(instruments, Constants.TRANSACTION_TYPE_BUY);
        System.out.println(triggerRangeMap.get("NSE:SBIN").lower);
        System.out.println(triggerRangeMap.get("NSE:APOLLOTYRE").upper);
        System.out.println(triggerRangeMap.get("BSE:INFY").percentage);
    }

    /**
     * Get orderbook.
     */
    public void getOrders(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get orders returns order model which will have list of orders inside, which can be accessed as follows,
        List<Order> orders = kiteConnect.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i).tradingSymbol + " " + orders.get(i).orderId + " " + orders.get(i).parentOrderId + " " + orders.get(i).orderType + " " + orders.get(i).averagePrice + " " + orders.get(i).exchangeTimestamp);
        }
        System.out.println("list of orders size is " + orders.size());
    }

    /**
     * Get order details
     */
    public void getOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        List<Order> orders = kiteConnect.getOrderHistory("180111000561605");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i).orderId + " " + orders.get(i).status);
        }
        System.out.println("list size is " + orders.size());
    }

    /**
     * Get tradebook
     */
    public void getTrades(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns tradebook.
        List<Trade> trades = kiteConnect.getTrades();
        for (int i = 0; i < trades.size(); i++) {
            System.out.println(trades.get(i).tradingSymbol + " " + trades.size());
        }
        System.out.println(trades.size());
    }

    /**
     * Get trades for an order.
     */
    public void getTradesWithOrderId(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns trades for the given order.
        List<Trade> trades = kiteConnect.getOrderTrades("180111000561605");
        System.out.println(trades.size());
    }

    /**
     * Modify order.
     */
    public void modifyOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = "ASHOKLEY";
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 122.25;

        Order order21 = kiteConnect.modifyOrder("180116000984900", orderParams, Constants.VARIETY_REGULAR);
        System.out.println(order21.orderId);
    }

    /**
     * Modify first leg bracket order.
     */
    public void modifyFirstLegBo(KiteConnect kiteConnect) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.price = 31.0;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.tag = "myTag";
        orderParams.triggerPrice = 0.0;

        Order order = kiteConnect.modifyOrder("180116000798058", orderParams, Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    public void modifySecondLegBoSLM(KiteConnect kiteConnect) throws KiteException, IOException {

        OrderParams orderParams = new OrderParams();
        orderParams.parentOrderId = "180116000798058";
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.triggerPrice = 30.5;
        orderParams.price = 0.0;
        orderParams.orderType = Constants.ORDER_TYPE_SLM;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;

        Order order = kiteConnect.modifyOrder("180116000812154", orderParams, Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    public void modifySecondLegBoLIMIT(KiteConnect kiteConnect) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.parentOrderId = "180116000798058";
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.quantity = 1;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 35.3;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;

        Order order = kiteConnect.modifyOrder("180116000812153", orderParams, Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    /**
     * Cancel an order
     */
    public void cancelOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.
        // Cancel order will return order model which will only have orderId.
        Order order2 = kiteConnect.cancelOrder("180116000727266", Constants.VARIETY_REGULAR);
        System.out.println(order2.orderId);
    }

    public void exitBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        Order order = kiteConnect.cancelOrder("180116000812153", "180116000798058", Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    /**
     * Get all positions.
     */
    public void getPositions(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get positions returns position model which contains list of positions.
        Map<String, List<Position>> position = kiteConnect.getPositions();
        System.out.println(position.get("net").size());
        System.out.println(position.get("day").size());
    }

    /**
     * Get holdings.
     */
    public void getHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get holdings returns holdings model which contains list of holdings.
        List<Holding> holdings = kiteConnect.getHoldings();
        System.out.println(holdings.size());
    }

    /**
     * Converts position
     */
    public void converPosition(KiteConnect kiteConnect) throws KiteException, IOException {
        //Modify product can be used to change MIS to NRML(CNC) or NRML(CNC) to MIS.
        JSONObject jsonObject6 = kiteConnect.convertPosition("ASHOKLEY", Constants.EXCHANGE_NSE, Constants.TRANSACTION_TYPE_BUY, Constants.POSITION_DAY, Constants.PRODUCT_MIS, Constants.PRODUCT_CNC, 1);
        System.out.println(jsonObject6);
    }

    /**
     * Get all instruments that can be traded using kite connect.
     */
    public void getAllInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get all instruments list. This call is very expensive as it involves downloading of large data dump.
        // Hence, it is recommended that this call be made once and the results stored locally once every morning before market opening.
        List<Instrument> instruments = kiteConnect.getInstruments();
        System.out.println(instruments.size());
    }

    /**
     * Get instruments for the desired exchange.
     */
    public void getInstrumentsForExchange(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get instruments for an exchange.
        List<Instrument> nseInstruments = kiteConnect.getInstruments("CDS");
        System.out.println(nseInstruments.size());
    }

    /**
     * Get quote for a scrip.
     */
    public void getQuote(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get quotes returns quote for desired tradingsymbol.
        String[] instruments = {"256265", "BSE:INFY", "NSE:APOLLOTYRE", "NSE:NIFTY 50"};
        Map<String, Quote> quotes = kiteConnect.getQuote(instruments);
        System.out.println(quotes.get("NSE:APOLLOTYRE").instrumentToken + "");
        System.out.println(quotes.get("NSE:APOLLOTYRE").oi + "");
        System.out.println(quotes.get("NSE:APOLLOTYRE").depth.buy.get(4).getPrice());
        System.out.println(quotes.get("NSE:APOLLOTYRE").timestamp);
    }

    /* Get ohlc and lastprice for multiple instruments at once.
     * Users can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}*/
    public void getOHLC(KiteConnect kiteConnect) throws KiteException, IOException {
        String[] instruments = {"256265", "BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
        System.out.println(kiteConnect.getOHLC(instruments).get("256265").lastPrice);
        System.out.println(kiteConnect.getOHLC(instruments).get("NSE:NIFTY 50").ohlc.open);
    }

    /**
     * Get last price for multiple instruments at once.
     * USers can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     */
    public static void getLTP(KiteConnect kiteConnect) throws KiteException, IOException {
        String[] instruments = {"256265", "BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
        System.out.println(kiteConnect.getLTP(instruments).get("256265").lastPrice);
    }

    /**
     * Get historical data for an instrument.
     */
    public static void getOpenRangeBreakDown(KiteConnect kiteConnect, String instrument, List<String> inTrendingStocks) throws KiteException, IOException {
        /** Get historical data dump, requires from and to date, intrument token, interval, continuous (for expired F&O contracts)
         * returns historical data object which will have list of historical data inside the object.*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final Date from = formatter.parse(LocalDate.now() + " 09:35:00");
            final Date to = formatter.parse(LocalDate.now() + " 9:45:00");
            HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, instrument, "minute", false);
            if (!historicalData.dataArrayList.isEmpty()) {
                double max = historicalData.dataArrayList.get(0).high;
                for (HistoricalData candle : historicalData.dataArrayList) {
                    if (candle.high > max) {
                        max = candle.high;
                    }
                }

                final Date from1 = formatter.parse(LocalDate.now() + " 09:46:00");
                final Date to1 = formatter.parse(LocalDate.now() + " 10:00:00");
                HistoricalData historicalDataAfterRange = kiteConnect.getHistoricalData(from1, to1, instrument, "minute", false);
                double max2 = 0;
                for (HistoricalData candle : historicalDataAfterRange.dataArrayList) {
                    if (candle.high > max2) {
                        max2 = candle.high;
                    }
                }
                if (max > max2) {
                    inTrendingStocks.add(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
                }
            }
        } catch (
                KiteException e) {
            e.printStackTrace();
            System.out.println(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
        } catch (
                ParseException e) {
            e.printStackTrace();
            System.out.println(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
        } catch (Exception inEx) {
            inEx.printStackTrace();
            System.out.println(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
        }

    }

    /**
     * Get historical data for an instrument.
     */
    public static void getOpenRangeBreakOut(KiteConnect kiteConnect, String instrument, List<String> inTrendingStocks) throws KiteException, IOException {
        /** Get historical data dump, requires from and to date, intrument token, interval, continuous (for expired F&O contracts)
         * returns historical data object which will have list of historical data inside the object.*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final Date from = formatter.parse("2018-11-09 09:35:00");
            final Date to = formatter.parse("2018-11-09 9:45:00");
            HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, instrument, "minute", false);
            if (!historicalData.dataArrayList.isEmpty()) {
                double min = historicalData.dataArrayList.get(0).low;
                for (HistoricalData candle : historicalData.dataArrayList) {
                    if (candle.low < min) {
                        min = candle.low;
                    }
                }

                final Date from1 = formatter.parse("2018-11-09 09:46:00");
                final Date to1 = formatter.parse("2018-11-09 10:00:00");
                HistoricalData historicalDataAfterRange = kiteConnect.getHistoricalData(from1, to1, instrument, "minute", false);
                double min2 = historicalDataAfterRange.dataArrayList.get(0).low;
                for (HistoricalData candle : historicalDataAfterRange.dataArrayList) {
                    if (candle.low < min2) {
                        min2 = candle.low;
                    }
                }
                if (min < min2) {
                    inTrendingStocks.add(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
                }
            }
        } catch (
                KiteException e) {
            e.printStackTrace();
            System.out.println(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
        } catch (
                ParseException e) {
            e.printStackTrace();
            System.out.println(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
        } catch (Exception inE) {
            inE.printStackTrace();
            System.out.println(NseInstruments.TRADE_INSTRUMENTS.get(instrument));
        }

    }


    /**
     * Logout user.
     */
    public void logout(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Logout user and kill session. */
        JSONObject jsonObject10 = kiteConnect.logout();
        System.out.println(jsonObject10);
    }
}
