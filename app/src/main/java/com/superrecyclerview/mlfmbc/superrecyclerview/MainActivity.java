package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import customheaderandfooter.CustomRecyclerView;
import customheaderandfooter.adapter.CommonAdapter;
import customheaderandfooter.interfaces.ConvertFooter;
import customheaderandfooter.interfaces.ConvertHeader;
import customheaderandfooter.interfaces.LoadMore;
import customheaderandfooter.viewholder.ViewHolder;
import customheaderandfooter.viewholder.ViewHolderFoot;
import customheaderandfooter.viewholder.ViewHolderHead;

public class MainActivity extends AppCompatActivity implements ConvertHeader, ConvertFooter, LoadMore {
    private CustomRecyclerView mCustomRecyclerView;
    private CommonAdapter mCommonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mCustomRecyclerView = (CustomRecyclerView) findViewById(R.id.cs_rv);
        mCustomRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mCommonAdapter = new CommonAdapter(this, R.layout.item) {
            @Override
            public int ItemViewType(int position) {
                return 0;
            }

            @Override
            public void convert(ViewHolder holder, Object o, int position) {
                ((TextView)holder.getView(R.id.position)).setText("我是第"+position+"条");
            }

            @Override
            public ViewHolder ViewHolderget(ViewGroup parent, int viewType) {
                return ViewHolder.get(mContext, parent, mLayoutId);
            }
        };
        mCustomRecyclerView.setAdapter(mCommonAdapter);
        mCommonAdapter.setmConvertHeader(this,R.layout.header);
        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<10;i++){
            data.add(""+i);
        }
        mCommonAdapter.addDatas(data);
        mCommonAdapter.setmConvertFooter(this, R.layout.footer);
        mCommonAdapter.setmLoadMore(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConvertHeadView(ViewHolderHead holder) {

    }

    @Override
    public void onConvertFooter(ViewHolderFoot holder, String loadingStr) {
        ((TextView)holder.getView(R.id.footer)).setText(loadingStr);
    }

    @Override
    public void onLoadMore() {
        mCommonAdapter.setIsShowLoading(true).setLoadingStr("正在加载").notifyLoadingChanged();
        mCustomRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> data=new ArrayList<String>();
                for(int i=0;i<10;i++){
                    data.add(""+i);
                }
                mCommonAdapter.addDatas(data);
                mCommonAdapter.setIsShowLoading(false).setLoadingStr("加载完成").notifyLoadingChanged();
            }
        },5000);
    }
}
