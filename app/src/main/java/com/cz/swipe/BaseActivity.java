package com.cz.swipe;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.cz.swipe.BaseActivity.java
 * @author: Czhen
 * @date: 2017-06-30 14:34
 */
public class BaseActivity extends AppCompatActivity {


    private SwipeBackLayout mBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        mBackLayout = new SwipeBackLayout(this);
        mBackLayout.attachActivity(this);

        FrameLayout rootView = (FrameLayout) findViewById(android.R.id.content);

        if (rootView.getChildCount() > 0) {
            View contentView = rootView.getChildAt(0);
            rootView.removeView(contentView);

            mBackLayout.addView(contentView);

            rootView.addView(mBackLayout);
        }
    }

    public SwipeBackLayout getBackLayout() {
        return mBackLayout;
    }

    /**
     *  设置滑动开关
     * @param enabled
     */
    public void setSwipeEnabled(boolean enabled){
        if(mBackLayout != null)
            mBackLayout.setEnabled(enabled);
    }

}
