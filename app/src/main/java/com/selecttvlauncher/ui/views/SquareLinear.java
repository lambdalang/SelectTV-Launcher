package com.selecttvlauncher.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/11/2016.
 */
public class SquareLinear extends LinearLayout {
    public SquareLinear(Context context) {
        super(context);
    }

    public SquareLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));

    }
}
