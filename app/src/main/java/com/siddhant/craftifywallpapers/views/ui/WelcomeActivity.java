package com.siddhant.craftifywallpapers.views.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.siddhant.craftifywallpapers.R;

public class WelcomeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress=0;
    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        try {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            int width1 = displayMetrics.widthPixels;

            int height1 = displayMetrics.heightPixels;
            TextView textView = findViewById(R.id.textViewdimen);
            textView.setText(width1 + " X " + height1);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);


                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
