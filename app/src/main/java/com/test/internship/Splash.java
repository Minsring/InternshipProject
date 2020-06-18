package com.test.internship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 2000);
    }

    private class splashHandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), User.class));
            Splash.this.finish();
        }
    }

    @Override
    // 초반 플래시 화면에서 넘어갈 때, 뒤로가기 버튼 못 누르게 함
    public void onBackPressed() { }
}
