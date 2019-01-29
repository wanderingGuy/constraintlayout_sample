package com.bftv.wanderingguy.constraint_layout_demo;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;

import hugo.weaving.DebugLog;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2019/1/26
 * @changeRecord [修改记录] <br/>
 */
public class MyConstraintLayout2 extends ConstraintLayout {
    public MyConstraintLayout2(Context context) {
        super(context);
    }

    public MyConstraintLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyConstraintLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @DebugLog
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("test_constraint","onMeasure");
    }

    @DebugLog
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("test_constraint","onLayout");
    }
}
