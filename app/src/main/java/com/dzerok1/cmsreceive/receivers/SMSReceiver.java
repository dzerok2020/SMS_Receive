package com.dzerok1.cmsreceive.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.StringTokenizer;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "TAG";
    private static final String DATA = "pdus";
    private static final String FORMAT = "format";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d(TAG, "Receiver: Called");
        Bundle data = intent.getExtras();
        if (data != null) {
            Object[] PDUS = (Object[]) data.get(DATA);
            String format = data.getString(FORMAT);
            // Data SMS Processing
            SmsMessage[] smsMessages = new SmsMessage[PDUS.length];
            for (int i = 0; i < PDUS.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    smsMessages[i] = SmsMessage.createFromPdu((byte[]) PDUS[i], format);
                } else {
                    smsMessages[i] = SmsMessage.createFromPdu((byte[]) PDUS[i]);
                }

                // Message processing
                String imcommingNumber = smsMessages[i].getDisplayOriginatingAddress();
                String smsContent = smsMessages[i].getMessageBody();
                StringTokenizer stringTokenizer = new StringTokenizer(smsContent);
                String key = stringTokenizer.nextToken(":");
                String body = "";
                if (stringTokenizer.hasMoreTokens()) {
                    body = stringTokenizer.nextToken(":");
                }
                if (key.equalsIgnoreCase("call")) {
                    call(context, body);
                } else if (key.equalsIgnoreCase("print")) {
                    Toast.makeText(context, "Message from " + imcommingNumber + " is " + body, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Invalid format", Toast.LENGTH_SHORT).show();
                }
//                Log.d(TAG, "Num: " + imcommingNumber + " Body: " + smsContent);
            }
        }


    }

    // Call
    private void call(Context context, String callNumber) {
        Log.d(TAG, "call: " + callNumber);
    }
}
