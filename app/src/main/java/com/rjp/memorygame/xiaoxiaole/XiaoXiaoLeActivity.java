package com.rjp.memorygame.xiaoxiaole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rjp.memorygame.R;

public class XiaoXiaoLeActivity extends Activity {

    private Context mContext;

    public static void trendTo(Context mContext) {
        Intent intent = new Intent(mContext, XiaoXiaoLeActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao_xiao_le);

        mContext = this;


    }
}
