package com.geek.aagamshah.capstone_project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.geek.aagamshah.capstone_project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeleprompterActivity extends AppCompatActivity {
    private Runnable count;
    private int countdown;
    private Handler customHandler;
    private boolean delayDone;
    private boolean killToast;
    private Runnable scroll;
    private int scrollSpeed;
    private boolean scrollText;
    private boolean showCountdown;
    private int time;
    private int timer;
    private Toast toast;

    @BindView(R.id.text_teleprompter)
    MirroredTextView mirroredTextView;
    @BindView(R.id.scrollview_teleprompter)
    ScrollView scrollView;

    public TeleprompterActivity() {
        scroll = new Runnable() {
            @Override
            public void run() {
                if(scrollText){
                    if(timer > 0){
                        timer = timer - 1;
                    }
                    else{
                        scrollView.scrollTo(0,scrollView.getScrollY() + 1);
                        timer = time;
                    }
                    customHandler.post(scroll);
                }
            }
        };
        count = new Runnable() {
            @Override
            public void run() {
                if (showCountdown) {
                    if (killToast) {
                        toast.cancel();
                    }
                    if (countdown > 0) {
                        toast = Toast.makeText(getBaseContext(), String.valueOf(countdown) + "...", Toast.LENGTH_SHORT);
                        toast.show();
                        customHandler.postDelayed(this, 1000);
                        countdown = countdown - 1;
                    } else {
                        if (killToast) {
                            toast.cancel();
                        }
                        delayDone = true;
                        scrollText = true;
                        customHandler.post(scroll);
                    }
                    killToast = true;
                } else if (countdown > 0) {
                    customHandler.postDelayed(this, 1000);
                    countdown = countdown - 1;
                } else {
                    delayDone = true;
                    scrollText = true;
                    customHandler.post(scroll);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teleprompter);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        String message = intent.getStringExtra(TypeActivity.EXTRA_TELETEXT);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        scrollSpeed = 102 - (prefs.getInt("pref_speed", 50)  + getResources().getInteger(R.integer.min_scrollspeed));
        MirroredTextView.mirror = prefs.getBoolean("pref_mirror", true);
        mirroredTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + prefs.getString("pref_font", "Roboto") + ".ttf"), 1);
        mirroredTextView.setTextSize((float) (prefs.getInt("pref_fontsize", 24) + getResources().getInteger(R.integer.min_fontsize)));
        mirroredTextView.setText(message);
        mirroredTextView.setTextColor(Color.parseColor(prefs.getString("pref_txtcolour","#FFFFFF")));
        mirroredTextView.setBackgroundColor(Color.parseColor(prefs.getString("pref_bgcolour", "#000000")));
        time = this.scrollSpeed * 100;
        scrollText = false;
        killToast = false;
        delayDone = true;
        scrollView.setBackgroundColor(Color.parseColor(prefs.getString("pref_bgcolour", "#000000")));
        scrollView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tap();
            }
        });
        customHandler = new Handler();
        if (intent.getBooleanExtra(TypeActivity.EXTRA_JUSTSTART, false)) {
            delayDone = true;
            scrollText = true;
            timer = this.time;
            customHandler.postDelayed(this.scroll, 500);
        }
        else if(prefs.getBoolean("pref_autostart",false)){
            delayDone = false;
            countdown = prefs.getInt("pref_startdelay",2)+1;
            showCountdown = prefs.getBoolean("pref_showCountdown",false);
            customHandler.post(count);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            tap();
        }
        return super.dispatchKeyEvent(event);
    }

    private void tap() {
        if (this.delayDone) {
            scrollText = !scrollText;
            customHandler.post(scroll);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(toast != null){
            toast.cancel();
        }
        if(customHandler != null){
            customHandler.removeCallbacksAndMessages(null);
        }
        scrollText = false;
    }
}

