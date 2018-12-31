package com.intradaytrend.timetrader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NseWatch {

    public static void main(String[] args) {
        String[] links = {"/live_market/dynaContent/live_watch/fomktwtch_FUTSTK.htm",
                "/live_market/dynaContent/live_watch/fomktwtch_OPTSTK.htm",
                // "/live_market/dynaContent/live_watch/fomktwtch_FOSPREAD.htm",
                // "/live_market/dynaContent/live_watch/fomktwtch_FUTIDXNIFTY.htm",
                "/live_market/dynaContent/live_watch/fomktwtch_OPTIDXNIFTY.htm",
                // "/live_market/dynaContent/live_watch/fomktwtch_FUTIDXNFTYMCAP50.htm",
                // "/live_market/dynaContent/live_watch/fomktwtch_OPTIDXNFTYMCAP50.htm",
                // "/live_market/dynaContent/live_watch/fomktwtch_FUTIDXBANKNIFTY.htm",
                "/live_market/dynaContent/live_watch/fomktwtch_OPTIDXBANKNIFTY.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_FUTIDXNIFTYINFRA.htm",
                //  "/live_market/dynaContent/live_watch/fomktwtch_OPTIDXNIFTYINFRA.htm",
                // "/live_market/dynaContent/live_watch/fomktwtch_FUTIDXNIFTYIT.htm",
                //  "/live_market/dynaContent/live_watch/fomktwtch_OPTIDXNIFTYIT.htm",
                //  "/live_market/dynaContent/live_watch/fomktwtch_FUTIDXNIFTYPSE.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_OPTIDXNIFTYPSE.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_FUTIDXMININFTY.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_OPTIDXMININFTY.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_AllCONTRACTS.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_FUTIDXS&P500.htm",
                //"live_market/dynaContent/live_watch/fomktwtch_OPTIDXS&P500.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_FUTIVXINDIAVIX.htm",//added by swapnil for VIX futures
                //"/live_market/dynaContent/live_watch/fomktwtch_FUTIDXFTSE100.htm",
                // "/live_market/dynaContent/live_analysis/most_active/fomktwtch_FUTIDXFTSE100.htm",
                //"/live_market/dynaContent/live_watch/fomktwtch_OPTIDXFTSE100.htm",
                // "/live_market/dynaContent/live_analysis/most_active/fomktwtch_OPTIDXFTSE100.htm",
                //  "/live_market/dynaContent/live_watch/fomktwtch_FUTIDXDJIA.htm"};
        };
        try {

            for (String v : links) {

                String html = "https://www1.nseindia.com" + v;
                Document doc = Jsoup.connect(html).get();
                Elements tableElements = doc.select("table");
                Elements tableRowElements = tableElements.select(":not(thead) tr");
                for (int i = 0; i < tableRowElements.size(); i++) {
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("td");
                    boolean print = false;
                    String action = "No Action";
                    double high = Double.parseDouble(rowItems.get(7).text().replaceAll(",", ""));
                    double open = Double.parseDouble(rowItems.get(6).text().replaceAll(",", ""));
                    double low = Double.parseDouble(rowItems.get(8).text().replaceAll(",", ""));
                    double last = Double.parseDouble(rowItems.get(9).text().replaceAll(",", ""));
                    if (open == high) {

                        if (last <= open) {
                            print = true;
                            action = "short    ";
                        }

                    } else if (open == low) {
                        if (last >= open) {
                            print = true;
                            action = "long     ";
                        }

                    }

                    if (print) {
                        System.out.print("Action" + "\t" + action);
                        System.out.print("\t" + "\t");
                        System.out.print("p.close" + "\t");
                        System.out.print("open" + "\t");
                        System.out.print("high" + "\t");
                        System.out.print("low" + "\t" + "\t");
                        System.out.println("l.price");
                        System.out.print(rowItems.get(1).text() + "\t");
                        System.out.print(rowItems.get(3).text() + "\t");
                        System.out.print(rowItems.get(4).text() + "\t");
                        System.out.print(rowItems.get(5).text() + "\t");
                        System.out.print(rowItems.get(6).text() + "\t");
                        System.out.print(rowItems.get(7).text() + "\t");
                        System.out.print(rowItems.get(8).text() + "\t");
                        System.out.println(rowItems.get(9).text() + "\t");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

