package com.law.hongcaphelp.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.law.hongcaphelp.common.Common;
import com.law.hongcaphelp.R;
import com.law.hongcaphelp.my_service.MyService2;

public class Main2Activity extends AppCompatActivity {

    Intent intent;
    TextView txtContent;
    WebView web;

    private boolean onOff = true;

    private Handler mHandler = new Handler();

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        intent = new Intent(this, MyService2.class);
//        txtContent = (TextView) findViewById(R.id.txt_content);

        web = (WebView) findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        web.setWebChromeClient(new WebBrowserClient());
        web.addJavascriptInterface(new JavaScriptMethods(), "sample");
        web.loadUrl(Common.LEAK_SERVER_URL);



        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);
            }
        });

/*        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.loadUrl(Common.LEAK_SERVER_URL);
            }
        });*/

        MyAsync myAsync = new MyAsync();
        myAsync.execute();
    }


    public class JavaScriptMethods {

        JavaScriptMethods() {

        }

        @android.webkit.JavascriptInterface
        public void clickOnFace() {
            mHandler.post(new Runnable() {
                public void run() {
                    web.loadUrl("javascript:changeFace()");
                }
            });

        }
    }

    final class WebBrowserClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("my", message);
            result.confirm();

            return true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onOff = false;
    }

    public class MyAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            txtContent.setText("from server : " + values[0]);
            web.loadUrl(Common.LEAK_SERVER_URL);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                while (onOff) {
                    Common common = Common.getInstance();
                    String str = common.connect(Common.POST_SERVER_URL);

                    Gson gson = new Gson();

                    publishProgress(str);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
