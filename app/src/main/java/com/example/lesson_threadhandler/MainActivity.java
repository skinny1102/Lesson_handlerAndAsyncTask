package com.example.lesson_threadhandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Handler handler;
    Button btnStart,btnStart2;
    TextView textView,tvView;
    int time = 10;

    public static final int MSG_COUNT_DOWN = 100;
    public static final int MSG_COUNT_DONE = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        handler();
    }
    private void getView(){
        btnStart = (Button) findViewById(R.id.btnStart);
        textView = (TextView) findViewById(R.id.textView);
        btnStart2 = (Button) findViewById(R.id.btnStar2);
        tvView = (TextView) findViewById(R.id.tvView2);
        btnStart.setOnClickListener(this);
        btnStart2.setOnClickListener(this);

    }
    private void handler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
               switch (msg.what) {
                   case MSG_COUNT_DOWN:
                       textView.setText(String.valueOf(msg.arg1));
                       break;
                   case MSG_COUNT_DONE:
                       textView.setText("Hoàn Thành ");
                       break;
                   default:
                       break;
               }
            }
        };
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStart:
                ///
                doCountDown();
                break;
            case R.id.btnStar2:
                doIncreateNumber();
                break;
            default:
                break;
        }
    }
    //Đếm
    private void doCountDown(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                do{
                    time--;
                    Message message = new Message();
                    message.what = MSG_COUNT_DOWN;
                    message.arg1 = time;
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (time>0);
                handler.sendEmptyMessage(MSG_COUNT_DONE);
            }

        });
        thread.start();

    }
    private  void doIncreateNumber(){
        MyAsync myAsync = new MyAsync();
        myAsync.execute();
    }
    private  class MyAsync extends AsyncTask<Void,Integer,Void>{
        private int count ;

        @Override
        protected void onPreExecute() {
            count = 0 ;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tvView.setText(String.valueOf(values[0]));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (count<=20){
                count ++;
                publishProgress(count );
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

}