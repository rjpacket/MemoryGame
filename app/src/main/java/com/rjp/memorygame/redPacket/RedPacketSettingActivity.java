package com.rjp.memorygame.redPacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.rjp.memorygame.R;
import com.rjp.memorygame.SPUtil;

public class RedPacketSettingActivity extends Activity {

    private Context mContext;

    public static void trendTo(Context mContext) {
        Intent intent = new Intent(mContext, RedPacketSettingActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_setting);

        mContext = this;

        LinearLayout llGetRedPacketLabel = findViewById(R.id.ll_auto_get_red_packet);
        boolean isOpen = (Boolean) SPUtil.getData(mContext, "auto-get-red-packet", false);
        llGetRedPacketLabel.setSelected(isOpen);

        llGetRedPacketLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                SPUtil.saveData(mContext, "auto-get-red-packet", v.isSelected());
            }
        });
    }
}
