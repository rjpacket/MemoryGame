package com.rjp.memorygame.xiaoxiaole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rjp.memorygame.R;

public class ListViewTestActivity extends Activity {

    private Context mContext;

    public static void trendTo(Context mContext) {
        Intent intent = new Intent(mContext, ListViewTestActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao_xiao_le);

        mContext = this;

        ListView listView = new ListView(mContext);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_view, null);
                }
                Log.d("------------>", "父getView----->" + position);
                TextView tvHeader = convertView.findViewById(R.id.tv_header);
                tvHeader.setText("这是第" + position + "个父");
                ListView childListView = convertView.findViewById(R.id.child_list_view);
                childListView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return 20;
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if(convertView == null){
                            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child_list_view, null);
                            convertView.setTag(position);
                            Log.d("------------>", "子填充getView----->" + convertView.getTag());
                        }else {
                            Log.d("------------>", "子复用getView----->" + convertView.getTag());
                        }
                        TextView tvChild = convertView.findViewById(R.id.tv_child_header);
                        tvChild.setText("Child" + position);
                        return convertView;
                    }
                });
                return convertView;
            }
        });
    }
}
