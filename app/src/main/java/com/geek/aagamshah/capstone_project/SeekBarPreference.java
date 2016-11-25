package com.geek.aagamshah.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.geek.aagamshah.capstone_project.activity.TeleprompterActivity;
import com.geek.aagamshah.capstone_project.activity.TypeActivity;

/**
 * Created by Aagam Shah on 11/24/2016.
 */

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private static final String aagamshah = "http://aagamshah.com";
    private boolean justStart;
    private Context mContext;
    private int mDefault;
    private String mDialogMessage;
    private int mMax;
    private int mMin;
    private SeekBar mSeekBar;
    private TextView mSplashText;
    private String mSuffix;
    private int mValue;
    private TextView mValueText;
    private Button previewButton;
    private boolean showPreview;
    private boolean showSize;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mValue = 0;
        mContext = context;
        mDialogMessage = attrs.getAttributeValue(androidns,"dialogMessage");
        mSuffix =attrs.getAttributeValue(androidns,"text");
        mDefault = attrs.getAttributeIntValue(androidns,"defaultValue",0);
        mMin = attrs.getAttributeIntValue(aagamshah,"min",0);
        mMax = attrs.getAttributeIntValue(androidns,"max",100) - mMin;
        if(attrs.getAttributeBooleanValue(aagamshah,"showSize",false)){
            showSize = true;
        }
        else{
            showSize = false;
        }
        if (attrs.getAttributeBooleanValue(aagamshah, "hasPreview", false)) {
            showPreview = true;
        } else {
            showPreview = false;
        }
        if (attrs.getAttributeBooleanValue(aagamshah, "justStart", false)) {
            justStart = true;
        } else {
            justStart = false;
        }
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);
        mSplashText = new TextView(mContext);
        if(mDialogMessage != null){
            mSplashText.setText(mDialogMessage);
        }
        layout.addView(mSplashText);
        mValueText = new TextView(mContext);
        mValueText.setGravity(View.TEXT_ALIGNMENT_CENTER);
        mValueText.setTextSize(32.0f);
        layout.addView(mValueText,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(shouldPersist()){
            mValue = getPersistedInt(mDefault);
        }
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
        if(showPreview){
            previewButton = new Button(getContext());
            previewButton.setText(R.string.button_preview);
            //TODO: Add OnClick Listener
            previewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),TeleprompterActivity.class);
                    intent.putExtra(TypeActivity.EXTRA_JUSTSTART,justStart);
                    intent.putExtra(TypeActivity.EXTRA_TELETEXT,getContext().getString(R.string.blind_text));
                    getContext().startActivity(intent);
                }
            });
            layout.addView(previewButton);
        }
        return layout;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if(restorePersistedValue){
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        }
        else{
            mValue = ((Integer)defaultValue).intValue();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String t = String.valueOf(mMin+progress);
        TextView textView = mValueText;
        if (mSuffix != null) {
            t = t.concat(mSuffix);
        }
        textView.setText(t);
        if(shouldPersist()){
            persistInt(progress);
        }
        callChangeListener(new Integer(progress));
        if(showSize){
            mValueText.setTextSize((float) (mMin + progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setMax(int mMax) {
        this.mMax = mMax;
    }

    public int getMax() {
        return mMax;
    }

    public void setProgress(int progress){
        if(mSeekBar != null){
            mSeekBar.setProgress(progress);
        }
    }

    public int getProgress(){
        return mValue + mMin;
    }
}
