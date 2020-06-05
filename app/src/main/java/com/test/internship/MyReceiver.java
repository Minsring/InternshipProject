package com.test.internship;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver(){ }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive 들어옴"); //여기 실행 x
        switch (intent.getAction()) {
            case Intent.ACTION_BATTERY_LOW:
                Toast.makeText(context, "배터리 부족", Toast.LENGTH_SHORT).show();
                break;
        }
        ///throw new UnsupportedOperationException("Not yet implemented");
    }
}


