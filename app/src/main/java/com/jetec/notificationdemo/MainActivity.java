package com.jetec.notificationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
//藍芽
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private String CHANNEL_ID = "Coder";
    //藍芽
    private  static  final  String TAG = "MainActivity";
    BluetoothUtil _BluetoothUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //藍芽
        _BluetoothUtil = new BluetoothUtil();
        _BluetoothUtil.registerBluetoothReceiver(this);
        Log.i(TAG, "onCreate: BT State : "+ _BluetoothUtil.getBlueToothState());
        _BluetoothUtil.colseBlueTooth();
        Log.i(TAG, "onCreate: BT State : "+ _BluetoothUtil.getBlueToothState());
        _BluetoothUtil.openBlueTooth();
        Log.i(TAG, "onCreate: BT State : "+ _BluetoothUtil.getBlueToothState());
        _BluetoothUtil.gotoSystem(this);

        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        /**初始化介面控件與點擊事件*/
        Button btDefault,btCustom;
        //btDefault = findViewById(R.id.button_DefaultNotification);
        btCustom = findViewById(R.id.button_CustomNotification);
        //btDefault.setOnClickListener(onDefaultClick);
        btCustom.setOnClickListener(onCustomClick);

    }

    //點選"系統預設通知" 用不到先刪除了 只用下面的客製化通知


    /**點選"客製化通知"*/
    public View.OnClickListener onCustomClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /**建立要嵌入在通知裡的介面*/
            RemoteViews view = new RemoteViews(getPackageName(),R.layout.custom_notification);

            /**初始化Intent，攔截點擊事件*/
            Intent intent = new Intent(MainActivity.this,NotificationReceiver.class);

            /**設置通知內"Hi"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Hi)*/
            intent.setAction("Hi");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this
                    ,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            /**設置通知內"Close"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Close)*/
            intent.setAction("Close");
            PendingIntent close = PendingIntent.getBroadcast(MainActivity.this
                    ,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            /**設置通知內的控件要做的事*/
            /*設置標題*/
            view.setTextViewText(R.id.textView_Title,"記得拔鑰匙！！！");
            /*設置圖片*/
            view.setImageViewResource(
                    R.id.imageView_Icon,R.drawable.ic_baseline_directions_bike_24);

            /*設置"Hi"按鈕點擊事件(綁pendingIntent)*/
            view.setOnClickPendingIntent(R.id.button_Noti_Hi,pendingIntent);
            /*設置"Close"按鈕點擊事件(綁close)*/
            view.setOnClickPendingIntent(R.id.button_Noti_Close,close);

            /**建置通知欄位的內容*/
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_directions_bike_24)
                    .setContent(view)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
            NotificationManagerCompat notificationManagerCompat
                    = NotificationManagerCompat.from(MainActivity.this);
            /**發出通知*/
            notificationManagerCompat.notify(1,builder.build());
            //發出聲音
            Intent startIntent = new Intent(MainActivity.this, RingtonePlayingService.class);
            MainActivity.this.startService(startIntent);
            //發出震動
            //playVibrate();
        }
    };

    public void playVibrate()
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 0, 1000, 500, 1000, 500, 1000, 500, 1000, 500};
        v.vibrate(pattern , -1);
    }

//

}//

