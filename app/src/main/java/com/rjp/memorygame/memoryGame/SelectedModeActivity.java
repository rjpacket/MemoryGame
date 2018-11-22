package com.rjp.memorygame.memoryGame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.rjp.memorygame.R;

public class SelectedModeActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_mode);

        mContext = this;

        findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayGameActivity.start(mContext, 5);
            }
        });

        findViewById(R.id.hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayGameActivity.start(mContext, 6);
            }
        });

        findViewById(R.id.insane).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayGameActivity.start(mContext, 7);
            }
        });
    }
}
