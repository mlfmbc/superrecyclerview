package customheaderandfooter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.superrecyclerview.mlfmbc.superrecyclerview.DensityUtil;

/**
 * Created by chang on 2016/11/7.
 */
public class MdRecyclerViewLeft extends RecyclerView{
    private float mLastDownY = 0f;
    private int mDistance = 0;
    private int index=0;
private Context context;
    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    private boolean isSelf=true;
    public MdRecyclerViewLeft(Context context) {
        this(context, null);
    }

    public MdRecyclerViewLeft(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MdRecyclerViewLeft(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
            return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastDownY = e.getY();
//                break;
//
//            case MotionEvent.ACTION_CANCEL:
//                break;
//
//            case MotionEvent.ACTION_UP:
//                mLastDownY = 0f;
//                mDistance = 0;
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                if (mLastDownY != 0f) {
//                    mDistance = (int) (mLastDownY - e.getY());
//
//                    if ((mDistance < 0 && !canScrollVertically(-1))
//                            || (mDistance > 0 && !canScrollVertically(1))) {
//
//                    }else{
//
//                    }
//                }
//                mDistance = 0;
//                break;
//        }
//        Log.e("onTouchEvent","onTouchEvent"+mDistance);
//        if(mDistance < 0 ){//&& !canScrollVertically(-1)
//            Log.e("向下","滑动");
//        }
//        if(getTop()> DensityUtil.dip2px(context, 50)){
//            isSelf=false;
//        }else{
//            isSelf=true;
//
////            if((!left_rv.canScrollVertically(-1) || (!left_rv.canScrollVertically(1)))&&left_rv.getTop()==DensityUtil.dip2px(context, 50)){
////                isSelf=false;
////            }
//        }
//        if(!isSelf){// 让后边的滑动
//            return false;
//        }else{
//            return super.onTouchEvent(e);//让自己滑动
//        }
//    }
}
