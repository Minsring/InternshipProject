package com.test.internship;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // throw new UnsupportedOperationException("Not yet implemented");
        switch (intent.getAction()) {
            case Intent.ACTION_BATTERY_LOW:
                Toast.makeText(context, "배터리 부족", Toast.LENGTH_SHORT).show();
        }
    }
}


