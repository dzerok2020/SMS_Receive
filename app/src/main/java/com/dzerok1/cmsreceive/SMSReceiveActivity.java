package com.dzerok1.cmsreceive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.dzerok1.cmsreceive.receivers.SMSReceiver;

public class SMSReceiveActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private static final int REQ_CODE = 99;
    private static SMSReceiver smsReceiver = new SMSReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_receive_layout);

        // Check and allow required oermissions
        if (!checkPermission(Manifest.permission.RECEIVE_SMS)) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQ_CODE);
        } else {
            performAction();
        }
    }

    private boolean checkPermission(String permission) {
        int check = checkSelfPermission(permission);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE) {
            if (permissions.length == grantResults.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "permission: Required permission are not allowed!");
                        return;
                    }
                }
                // All required permission are allowed
                performAction();
            }
        }
    }

    private void performAction() {
        // Registering of SMSReceive
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
    }
}