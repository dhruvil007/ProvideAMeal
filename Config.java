package com.codeshastra.coderr.provideameal;

public class Config {
    // flag to identify whether to show single line
    // or multi line text in push notification tray
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    public static int flag = 0;
    public static String token;
    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final int NOTIFICATION_ID = 100;
}