package com.law.hongcaphelp.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.law.hongcaphelp.R;

public class MainActivity extends AppCompatActivity {

    private Messenger mRemote;
    private Intent intent;

    private TextView txtContent;

    private Context mContext;

    private boolean mIsBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        intent = new Intent(this, MyService.class);

        txtContent = (TextView) findViewById(R.id.txt);

        //서비스 시작
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "start");
                startService(intent);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                mIsBound = true;
            }
        });

        //서비스 종료
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "stop");

                if(mIsBound){
                    stopService(intent);
                    unbindService(mConnection);
                    mIsBound = false;
                }

            }
        });

        //서비스한테 뭐 보냄
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "stop");
                testSendMessage();

            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemote = new Messenger(service); //메신저한테 이 서비스에게 보낼거야 말해줌

            if(mRemote != null){
                Message msg = new Message();
                msg.what = 0;
                msg.obj = new Messenger(new RemoteHandler());
                try{
                    mRemote.send(msg);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemote = null;
        }
    };

    private  class RemoteHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Log.e("rece", msg.obj.toString());
            txtContent.setText(msg.obj.toString());
            Toast.makeText(mContext, "hi"+msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // Test 용 메세지 보내기
    public void testSendMessage() {
        if (mRemote != null) {
            Message msg = new Message();
            msg.what = 1;
            try {
                mRemote.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


}
