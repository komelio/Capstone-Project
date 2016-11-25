package com.geek.aagamshah.capstone_project.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aagam Shah on 11/24/2016.
 */

public class MirroredTextView extends TextView {
    public static boolean mirror;

    public MirroredTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        if (mirror) {
            canvas.translate((float) getWidth(), 0.0f);
            canvas.scale(-1.0f, 1.0f);
        }
        super.onDraw(canvas);
    }
}
