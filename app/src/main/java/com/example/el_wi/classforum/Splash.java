package com.example.el_wi.classforum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mImageView =(ImageView) findViewById(R.id.logoApp);
        mTextView = (TextView) findViewById(R.id.logoName);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        mImageView.startAnimation(animation);
        mTextView.startAnimation(animation);
        final Intent loginActivity = new Intent(Splash.this, LoginActivity.class);
        Thread timer = new Thread(){
            public void run (){
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(loginActivity);
                    finish();
                }
            }
        };
            timer.start();
    }
}