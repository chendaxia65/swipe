package com.cz.swipe;

import android.app.Activity;
import android.app.ActivityThread;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    ListView mListView;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new SimpleAdapter(this, getData(), R.layout.simple_layout, new String[]{"title"}, new int[]{R.id.tv_name}));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, " click position " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(new ViewPagerAdapter(getViewPagerData()));

    }

    public void onClickStart(View v){
        startActivity(NextActivity.newIntent(this));
    }

    public void onClick(View view) {
        Toast.makeText(MainActivity.this, " click ", Toast.LENGTH_SHORT).show();
    }


    private class ViewPagerAdapter extends PagerAdapter {

        private List<View> viewList;

        public ViewPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (viewList.size() > position)
                container.removeView(viewList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private List<View> getViewPagerData() {
        List<View> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setText("  viewPager " + i);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.onClick(v);
                }
            });
            list.add(textView);
        }

        return list;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "小宗");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "貂蝉");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "奶茶");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "大黄");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "hello");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        list.add(map);

        return list;
    }
}
