package com.cz.swipe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

/**
 * 侧滑返回
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.cz.swipe.SwipeBackLayout.java
 * @author: Czhen
 * @date: 2017-06-29 17:41
 */
public class SwipeBackLayout extends FrameLayout {

    private static final String TAG = "SwipeBackLayout";

    private static final int INVALID_POINTER = -1;

    private final int ALLOWED_SLIDING_DIRECTION_NOT = -1; //两个方向都不可滑动
    private final int ALLOWED_SLIDING_DIRECTION_LEFT = 0;
    private final int ALLOWED_SLIDING_DIRECTION_RIGHT = 1;
    private final int ALLOWED_SLIDING_DIRECTION_BOTH = 2;//两个方向都可滑动


    private int mActivePointerId = INVALID_POINTER;

    private int mTouchSlop;

    private boolean intercept = true;

    private float mLastMotionX;
    private float mLastMotionY;

    private OnSwipeCallback swipeCallback;

    private boolean isEnabled = true;
    private Activity mActivity;

    /**
     * 允许滑动的方向
     */
    private int allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_NOT;

    public SwipeBackLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 绑定Activity
     *
     * @param activity
     */
    public void attachActivity(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 是否开启Swipe
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }


    public void setSwipeCallback(OnSwipeCallback swipeCallback) {
        this.swipeCallback = swipeCallback;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled)
            return false;

        int action = ev.getAction();

        boolean hasConflictView = findConflictView(ev.getX(), ev.getY(), this);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                mLastMotionX = x;
                mLastMotionY = y;

                mActivePointerId = ev.getPointerId(0);

                intercept = false;
                break;

            case MotionEvent.ACTION_MOVE:

                final int activePointerId = mActivePointerId;
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                float deltaX = ev.getX(pointerIndex) - mLastMotionX;
                float deltaY = ev.getY(pointerIndex) - mLastMotionY;

                // 存在 滑动冲突的View，View可滚动方向为右，用户在屏幕上向左活动时，不拦截事件，交由View处理
                if (hasConflictView && allowedSlidingDirection == ALLOWED_SLIDING_DIRECTION_RIGHT && deltaX < 0) {
                    return false;
                }

                if (hasConflictView && allowedSlidingDirection == ALLOWED_SLIDING_DIRECTION_LEFT && deltaX > 0) {
                    return false;
                }

                if (hasConflictView && allowedSlidingDirection == ALLOWED_SLIDING_DIRECTION_BOTH) {
                    return false;
                }

                if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX > mTouchSlop) {
                    mLastMovePointX = -1;
                    brokenSlidingRules = false;
                    intercept = true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                intercept = false;
                break;
        }
        return intercept;
    }

    /**
     * 查找滑动冲突的 View,并确认 View可滑动的方向
     * 根据方向的不同判断是否拦截TouchEvent
     *
     * @param x
     * @param y
     * @param groupView
     * @return
     */
    private boolean findConflictView(float x, float y, ViewGroup groupView) {
        for (int i = 0; i < groupView.getChildCount(); i++) {
            View childView = groupView.getChildAt(i);

            if (isTouchPointInView(x, y, childView)) {
                if (childView instanceof HorizontalScrollView) {
                    if (ViewCompat.canScrollHorizontally(childView, -1)
                            && ViewCompat.canScrollHorizontally(childView, 1)) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_BOTH;
                    } else if (ViewCompat.canScrollHorizontally(childView, -1)) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_LEFT;
                    } else if (ViewCompat.canScrollHorizontally(childView, 1)) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_RIGHT;
                    } else {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_NOT;
                    }
                    return true;
                } else if (childView instanceof ViewPager) {
                    int itemCount = ((ViewPager) childView).getAdapter().getCount();
                    int currentItemPosition = ((ViewPager) childView).getCurrentItem();
                    if ((currentItemPosition > 0 && currentItemPosition < (itemCount - 1))) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_BOTH;
                    } else if (itemCount <= 1) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_NOT;
                    } else if (currentItemPosition == (itemCount - 1)) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_LEFT;
                    } else if (currentItemPosition == 0) {
                        allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_RIGHT;
                    }
                    return true;
                } else if (childView instanceof RecyclerView) {
                    RecyclerView recyclerView = ((RecyclerView) childView);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager.canScrollHorizontally()) {
                        if (ViewCompat.canScrollHorizontally(childView, -1)
                                && ViewCompat.canScrollHorizontally(childView, 1)) {
                            allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_BOTH;
                        } else if (ViewCompat.canScrollHorizontally(childView, -1)) {
                            allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_LEFT;
                        } else if (ViewCompat.canScrollHorizontally(childView, 1)) {
                            allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_RIGHT;
                        } else {
                            allowedSlidingDirection = ALLOWED_SLIDING_DIRECTION_NOT;
                        }
                        return true;
                    }

                } else if (childView instanceof ViewGroup) {
                    return findConflictView(x, y, (ViewGroup) childView);
                }
            }
        }

        return false;
    }


    /**
     * @param x
     * @param y
     * @param child
     * @return
     */
    public boolean isTouchPointInView(float x, float y, View child) {
        return pointInView(x, y, 0, child);
    }

    /**
     * 根据当前点击的 X、Y坐标找到对应的 View
     *
     * @param localX
     * @param localY
     * @param slop
     * @param child
     * @return
     */
    private boolean pointInView(float localX, float localY, float slop, View child) {
        int[] location = new int[2];
        child.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        float right = left + child.getMeasuredWidth();
        float bottom = top + child.getMeasuredHeight();

        return localX >= -slop && localY >= -slop
                && localX <= (right + slop) && localX >= (left + slop)
                && localY <= (bottom + slop) && localY >= (top + slop);
    }


    private float mLastMovePointX = -1;
    private boolean brokenSlidingRules = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (brokenSlidingRules || !isEnabled)
            return false;

        int action = event.getAction();
        int activePointerIndex;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();

                mActivePointerId = event.getPointerId(0);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                activePointerIndex = event.findPointerIndex(mActivePointerId);

                if (activePointerIndex < 0) {
                    return false;
                }

                float moveX = event.getX(activePointerIndex);

                if (mLastMovePointX > 0 && mLastMovePointX > moveX) {
                    brokenSlidingRules = true;
                    return false;
                }
                mLastMovePointX = moveX;
                break;

            case MotionEvent.ACTION_UP:
                activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex < 0) {
                    return false;
                }
                float deltaX = event.getX(activePointerIndex) - mLastMotionX;
                float deltaY = event.getY(activePointerIndex) - mLastMotionY;

                mActivePointerId = INVALID_POINTER;

                //计算弧度表示的角
                double arcValue = Math.atan(Math.abs(deltaY) / Math.abs(deltaX));

                //用角度表示的角
                double angle = Math.toDegrees(arcValue);

                // 水平滑动的距离大于竖直滑动的距离 || 滑动起点、终点的夹角小于75度 && 水平滑动距离大于可识别的滑动距离
                if (((Math.abs(deltaX) > Math.abs(deltaY)) || (angle < 75.0)) && deltaX > mTouchSlop) {

                    // 如果监听了滑动返回事件则处理回调，不直接finish当前Activity
                    if (swipeCallback != null) {
                        swipeCallback.onSwipe();
                        return true;
                    }

                    if (mActivity != null)
                        mActivity.onBackPressed();

                    return true;
                }
                return false;
            case MotionEvent.ACTION_CANCEL:
                return false;

        }
        return true;
    }

    public interface OnSwipeCallback {

        void onSwipe();
    }
}
