package com.law.hongcaphelp.broad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.law.hongcaphelp.my_service.MyService2;

/**
 * Created by GDGO on 2016-04-13.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
//
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")){

            Intent myIntent = new Intent(context, MyService2.class);
            context.startService(myIntent);
        }
    }
}
