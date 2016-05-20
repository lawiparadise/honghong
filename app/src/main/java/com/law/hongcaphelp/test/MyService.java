package com.law.hongcaphelp.test;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.law.hongcaphelp.common.Common;

/**
 * Created by GDGO on 2016-04-13.
 */
public class MyService extends Service implements Runnable{

    private int count = 0;
    private boolean onOff = true;
    private Messenger mRemote;

    public void onCreate(){
        super.onCreate();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try{
            while(onOff){
                Common common = Common.getInstance();
                String str = common.connect(Common.POST_SERVER_URL);
                remoteSendMessage(str); //MainActivity로 전송

  //              Log.e("my url content",str);
//                Log.e("run", "my service"+count++);

                Thread.sleep(5000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        onOff = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(new RemoteHandler()).getBinder();
    }

    private  class RemoteHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mRemote = (Messenger) msg.obj; //리모트 핸들러가 들어간 메신저를 공유한다.
                    break;
                default:
                    remoteSendMessage("testhi");
                    break;
            }
        }
    }

    //여기서 보내면 MainActivity의 RemoteHandler에서 받는다.
    public void remoteSendMessage(String data){
        if(mRemote != null){
            Message msg = new Message();
            msg.what = 1;
            msg.obj = data;
            try{
                mRemote.send(msg);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
