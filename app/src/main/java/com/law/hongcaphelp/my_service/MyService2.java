package com.law.hongcaphelp.my_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.law.hongcaphelp.common.Common;
import com.law.hongcaphelp.R;
import com.law.hongcaphelp.dto.dtoList;
import com.law.hongcaphelp.main.Main2Activity;

import java.util.ArrayList;

/**
 * Created by GDGO on 2016-04-14.
 */
public class MyService2 extends Service {

    public boolean onOff = true;
    private NotificationManager manager;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        onOff = true;
        context = this;

        Thread thread = new Thread(runnable);
        thread.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onOff = false;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try{
                while(onOff){
                    //
                    Common common = Common.getInstance();
                    String str = common.connect(Common.LEAK_SERVER_URL);
//                    Log.e("runnable", str);
                    Gson gson = new Gson();
                    dtoList list = gson.fromJson(str, dtoList.class);

                    //
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(context, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setContentTitle("leaks status")
                            .setTicker("New Message")
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent);

                    //
                    String etc = list.getList().get(list.getList().size()-1).getEtc();
                    if(String.valueOf(etc).equals("normal")){
                        builder.setSmallIcon(R.drawable.green_circle).setContentText("normal");
                    } else{
                        builder.setSmallIcon(R.drawable.red_circle).setContentText(etc);
                    }

                    Notification notification = builder.build();

                    if(String.valueOf(str).equals("fail")){
                        Log.e("runnable", "fail..");
                    } else if(!String.valueOf(str).equals("fail")){
                        Log.e("runnable", "success..");
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        manager.notify(100, notification);
                    }

                    Thread.sleep(5000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
