package com.jetec.notificationdemo;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;;
import android.media.RingtoneManager;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /**接收點擊事件*/
        switch (intent.getAction()){
            case "Hi":
                Toast.makeText(context, "哈囉！", Toast.LENGTH_SHORT).show();
                break;
            case "Close":
                //關閉通知
                NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(1);
                //停止鈴聲
                Intent stopIntent = new Intent(context, RingtonePlayingService.class);
                context.stopService(stopIntent);
                break;
        }
    }
    RingtoneManager ringtoneManager;
}
