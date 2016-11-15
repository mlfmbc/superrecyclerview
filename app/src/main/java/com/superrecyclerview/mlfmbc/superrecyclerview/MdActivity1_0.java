package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import customheaderandfooter.CustomCoordinatorLayout;
import customheaderandfooter.CustomCoordinatorLayout1_0;
import customheaderandfooter.adapter.CommonAdapter;
import customheaderandfooter.interfaces.ConvertHeader;
import customheaderandfooter.viewholder.ViewHolder;
import customheaderandfooter.viewholder.ViewHolderHead;

/**
 * Created by 员工 on 2016/11/8.
 */
public class MdActivity1_0 extends AppCompatActivity implements ConvertHeader {
    private RecyclerView mCustomRecyclerView;
    private CommonAdapter mCommonAdapter, left_Adapter;
    private RecyclerView left_rv;
    CustomCoordinatorLayout1_0 mCustomCoordinatorLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_1_0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            toolbar.setFitsSystemWindows(true);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setTranslucent(this);
        mCustomRecyclerView = (RecyclerView) findViewById(R.id.cs_rv);
        mCustomRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mCommonAdapter = new CommonAdapter(this, R.layout.item) {
            @Override
            public int ItemViewType(int position) {
                return 0;
            }

            @Override
            public void convert(ViewHolder holder, Object o, int position) {
                ((TextView) holder.getView(R.id.position)).setText("我是第" + position + "条");
            }

            @Override
            public ViewHolder ViewHolderget(ViewGroup parent, int viewType) {
                return ViewHolder.get(mContext, parent, mLayoutId);
            }
        };
        mCustomRecyclerView.setAdapter(mCommonAdapter);
//        mCommonAdapter.setmConvertHeader(this, R.layout.header_md);
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            data.add("" + i);
        }
        mCommonAdapter.addDatas(data);
        initMd();
    }
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//            }

            ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
            for (int i = 0, count = parent.getChildCount(); i < count; i++) {
                View childView = parent.getChildAt(i);
                if (childView instanceof ViewGroup) {
                    childView.setFitsSystemWindows(true);
                    ((ViewGroup)childView).setClipToPadding(true);
                }
            }


        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void initMd() {
        mCustomCoordinatorLayout = (CustomCoordinatorLayout1_0) findViewById(R.id.mcod_layout);
        left_rv = (RecyclerView) findViewById(R.id.left_rv);
        left_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        left_Adapter = new CommonAdapter(this, R.layout.item) {
            @Override
            public int ItemViewType(int position) {
                return 0;
            }

            @Override
            public void convert(ViewHolder holder, Object o, int position) {
                ((TextView) holder.getView(R.id.position)).setText("选第" + position + "条");
            }

            @Override
            public ViewHolder ViewHolderget(ViewGroup parent, int viewType) {
                return ViewHolder.get(mContext, parent, mLayoutId);
            }
        };
        left_rv.setAdapter(left_Adapter);
        ArrayList<String> left_data = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            left_data.add("" + i);
        }
        left_Adapter.addDatas(left_data);
//        left_Adapter.setmConvertHeader(this, R.layout.header_md);
        left_Adapter.setOnItemClick(new CommonAdapter.onItemClick() {
            @Override
            public void onItemClick(int position) {
//                mCustomCoordinatorLayout.moveToPosition(position == mCommonAdapter.getItemCount() - 1 ? mCommonAdapter.getItemCount() - 1 : position * 5 + 1);
//                mCustomCoordinatorLayout.scrollToPositionWithOffset(position == mCommonAdapter.getItemCount() - 1 ? mCommonAdapter.getItemCount() - 1 : position * 5 + 1, DensityUtil.dip2px(MdActivity.this, 50));

                mCustomCoordinatorLayout.smoothMoveToPosition(position==mCommonAdapter.getItemCount()-1?mCommonAdapter.getItemCount()-1:position*5+1);
            }
        });
    }

    @Override
    public void onConvertHeadView(ViewHolderHead holder) {

    }
}
