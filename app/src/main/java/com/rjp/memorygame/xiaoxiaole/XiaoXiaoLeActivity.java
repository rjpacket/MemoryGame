package com.rjp.memorygame.xiaoxiaole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rjp.memorygame.R;
import com.rjp.memorygame.memoryGame.MemoryGameView;
import com.rjp.memorygame.memoryGame.OnMemoryGameListener;

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

        MemoryGameView gameView = findViewById(R.id.mgame);
//        gameView.start();
        gameView.setOnMemoryGameListener(new OnMemoryGameListener() {
            @Override
            public void gameOver() {

            }

            @Override
            public void gameSuccess(String level) {
                Toast.makeText(mContext, level, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
