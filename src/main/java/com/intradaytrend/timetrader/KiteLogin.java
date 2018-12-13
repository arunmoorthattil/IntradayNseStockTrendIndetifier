package com.intradaytrend.timetrader;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class KiteLogin {
    private KiteConnect kiteConnect;

    public void login() {
        try {
            // First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteconnect api call.
            // Initialize KiteSdk with your apiKey.
            kiteConnect = new KiteConnect("m74g3wpng8r6z7we");

            // set userId
            kiteConnect.setUserId("ZS5711");
            //Enable logs for debugging purpose. This will log request and response.
            kiteConnect.setEnableLogging(false);

            // Get login url
            String url = kiteConnect.getLoginURL();
            System.out.println(url);

            // Set session expiry callback.
            kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    System.out.println("session expired");
                }
            });

            User user = kiteConnect.generateSession("2yNT6BuAHIoye2k26GIAE5vjTyvPpWXq", "nz72vke7y14o5qgpzibfhssqyvts74kx");
            kiteConnect.setAccessToken(user.accessToken);
            kiteConnect.setPublicToken(user.publicToken);
        } catch (KiteException | IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public KiteConnect getKiteConnect() {
        return kiteConnect;
    }

    private static final Logger LOGGER = Logger.getLogger(KiteLogin.class);
}
