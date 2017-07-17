package com.cz.swipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.cz.swipe.NextActivity.java
 * @author: Czhen
 * @date: 2017-06-30 10:52
 */
public class NextActivity extends BaseActivity {

    RecyclerView mRecyclerView1;
    RecyclerView mRecyclerView2;
    RecyclerView mRecyclerView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        mRecyclerView1 = (RecyclerView) findViewById(R.id.recycler_1);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler_2);
        mRecyclerView3 = (RecyclerView) findViewById(R.id.recycler_3);


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);


        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2);
        layoutManager2.setOrientation(GridLayoutManager.HORIZONTAL);

        StaggeredGridLayoutManager layoutManager3 = new StaggeredGridLayoutManager(2, OrientationHelper.HORIZONTAL);


        mRecyclerView1.setLayoutManager(layoutManager1);
        mRecyclerView1.setAdapter(new RecyclerAdapter1(this));

        mRecyclerView2.setLayoutManager(layoutManager2);
        mRecyclerView2.setAdapter(new RecyclerAdapter1(this));

        mRecyclerView3.setLayoutManager(layoutManager3);
        mRecyclerView3.setAdapter(new RecyclerAdapter1(this));
    }


    private class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.TViewHolder> {

        Context mContext;

        public RecyclerAdapter1(Context context) {
            this.mContext = context;
        }

        @Override
        public TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.activity_next_linear, null);

            return new TViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TViewHolder holder, int position) {

            holder.tvText.setText(" horizontal linearLayout " + position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class TViewHolder extends RecyclerView.ViewHolder {

            TextView tvText;

            public TViewHolder(View itemView) {
                super(itemView);
                tvText = (TextView) itemView.findViewById(R.id.tv_text);
                tvText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, " click ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NextActivity.class);
    }
}
