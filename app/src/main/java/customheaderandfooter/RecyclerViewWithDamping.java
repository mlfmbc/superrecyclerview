package customheaderandfooter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 带阻尼效果的RecyclerView
 * Created by chang on 2016/11/3.
 */
public class RecyclerViewWithDamping extends RecyclerView implements Runnable {
    private float mLastDownY = 0f;
    private int mDistance = 0;
    private int mStep = 0;
    private boolean mPositive = false;

    interface Damping {
        void DampingMoving(int Distance);
        void DampingMoving();
    }

    public RecyclerViewWithDamping.Damping getDamping() {
        return Damping;
    }

    public void setDamping(RecyclerViewWithDamping.Damping damping) {
        Damping = damping;
    }

    private Damping Damping;

    public RecyclerViewWithDamping(Context context) {
        this(context, null);
    }

    public RecyclerViewWithDamping(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewWithDamping(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mLastDownY == 0f && mDistance == 0) {
                    mLastDownY = event.getY();
                    return true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP:
                if (mDistance != 0) {
                    mStep = 1;
                    mPositive = (mDistance >= 0);
                    this.post(this);
                    return true;
                }
                mLastDownY = 0f;
                mDistance = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mLastDownY != 0f) {
                    mDistance = (int) (mLastDownY - event.getY());

                    if ((mDistance < 0 && !canScrollVertically(-1))
                            || (mDistance > 0 && !canScrollVertically(1))) {
                        mDistance /= 3;
                        this.setTranslationY(-mDistance);
                        if(Damping!=null){
                            Damping.DampingMoving(-mDistance);
                        }
                        return true;
                    }
                }
                mDistance = 0;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mLastDownY == 0f && mDistance == 0) {
                    mLastDownY = event.getY();
                    return true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP:
                if (mDistance != 0) {
                    mStep = 1;
                    mPositive = (mDistance >= 0);
                    this.post(this);
                    return true;
                }
                mLastDownY = 0f;
                mDistance = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mLastDownY != 0f) {
                    mDistance = (int) (mLastDownY - event.getY());
                    if ((mDistance < 0 && !canScrollVertically(-1))
                            || (mDistance > 0 && !canScrollVertically(1))) {
                        mDistance /= 3;
                        this.setTranslationY(-mDistance);
                        if(Damping!=null){
                            Damping.DampingMoving(-mDistance);
                        }
                        return true;
                    }
                }
                mDistance = 0;
                break;
        }
        return super.onTouchEvent(event);
    }


    public void run() {
        if (mDistance < 0 && Math.abs(mDistance) > 50) {
            if(Damping!=null){
                Damping.DampingMoving();
            }
            mDistance = 0;
            mLastDownY = 0f;
            return;
        }
        mDistance += mDistance > 0 ? -mStep : mStep;
        ObjectAnimator OA = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, this.getTranslationY(), -mDistance);
        OA.setDuration(200);
        OA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Damping != null) {
                    Damping.DampingMoving((int) RecyclerViewWithDamping.this.getTranslationY());
                }
            }
        });
        OA.start();

        if ((mPositive && mDistance <= 0) || (!mPositive && mDistance >= 0)) {
            ObjectAnimator OA1 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, this.getTranslationY(), -0);
            OA1.setDuration(200);
            OA1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (Damping != null) {
                        Damping.DampingMoving((int) RecyclerViewWithDamping.this.getTranslationY());
                    }
                }
            });
            OA1.start();
            mDistance = 0;
            mLastDownY = 0f;

            return;
        }
        mStep += 1;
        this.post(this);
    }
}
