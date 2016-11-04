package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 员工 on 2016/11/2.
 */
public class RecyclerViewYDistanceWithLinearLayoutManager {
    private int LastPosition=0;
    private float LastDistance=0;
    private Map<Integer, Float> itemHeights=new HashMap<>();
    /**
     * 粗略计算滑动距离 在 所有item一样的情况下没有误差 其他情况 都有误差
     *
     * @param target
     * @return
     */
    public int getRoughScollYDistance(RecyclerView target) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) target.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    /**
     * 获取 精确 y轴滑动距离
     * @param target
     * @return
     */
    public float getAccurateScollYDistance(RecyclerView target) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) target.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        float itemHeight = firstVisiableChildView.getHeight();
        itemHeights.put(position,itemHeight);

        if(LastPosition!=position){
//            LastDistance=0;
            LastPosition=position;
            for (int i=0;i<=position;i++){
                LastDistance= itemHeights.get(position)+LastDistance;
            }
        }
        return LastDistance - firstVisiableChildView.getTop();
    }
}
